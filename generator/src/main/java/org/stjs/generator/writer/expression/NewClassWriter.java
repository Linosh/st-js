package org.stjs.generator.writer.expression;

import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.newExpression;
import static org.stjs.generator.writer.JavaScriptNodes.object;
import static org.stjs.generator.writer.JavaScriptNodes.paren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;

public class NewClassWriter implements VisitorContributor<NewClassTree, List<AstNode>, GenerationContext> {
	private BlockTree getDoubleBracesBlock(NewClassTree tree) {
		if (tree.getClassBody() == null) {
			return null;
		}
		for (Tree member : tree.getClassBody().getMembers()) {
			if (member instanceof BlockTree) {
				// XXX I should be sure it's not the one generated by the compiler
				BlockTree block = (BlockTree) member;
				if (!block.isStatic()) {
					return block;
				}
			}
		}
		return null;
	}

	private String getPropertyName(ExpressionTree var) {
		if (var instanceof IdentifierTree) {
			return ((IdentifierTree) var).getName().toString();
		}
		if (var instanceof MemberSelectTree) {
			return ((MemberSelectTree) var).getIdentifier().toString();
		}
		// TODO exception!?
		return null;
	}

	/**
	 * special construction for object initialization new Object(){{x = 1; y = 2; }};
	 */
	private List<AstNode> getObjectInitializer(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		BlockTree initBlock = getDoubleBracesBlock(tree);
		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		if (initBlock == null && !JavaNodes.isSyntheticType(type)) {
			return null;
		}

		List<AstNode> names = new ArrayList<AstNode>();
		List<AstNode> values = new ArrayList<AstNode>();
		if (initBlock != null) {
			for (StatementTree stmt : initBlock.getStatements()) {
				// TODO check the right type of statements x=y
				AssignmentTree assign = ((AssignmentTree) ((ExpressionStatementTree) stmt).getExpression());
				names.add(name(getPropertyName(assign.getVariable())));
				values.add(visitor.scan(assign.getExpression(), context).get(0));
			}
		}
		return Collections.<AstNode>singletonList(object(names, values));
	}

	private List<AstNode> getInlineFunctionDeclaration(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		// special construction for inline function definition
		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		if (!JavaNodes.isJavaScriptFunction(type)) {
			return null;
		}

		// here there should be a check that verified the existence of a single method (first is the generated
		// constructor)
		Tree method = tree.getClassBody().getMembers().get(1);
		return visitor.scan(method, context);
	}

	private List<AstNode> getAnonymousClassDeclaration(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		if (tree.getClassBody() == null) {
			return null;
		}

		List<AstNode> typeDeclaration = visitor.scan(tree.getClassBody(), context);

		return Collections.<AstNode>singletonList(newExpression(paren(typeDeclaration.get(0)), arguments(visitor, tree, context)));
	}

	private List<AstNode> arguments(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		List<AstNode> arguments = new ArrayList<AstNode>();
		for (Tree arg : tree.getArguments()) {
			arguments.addAll(visitor.scan(arg, context));
		}
		return arguments;
	}

	private List<AstNode> getRegularNewExpression(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		return Collections.<AstNode>singletonList(newExpression(name(context.getNames().getTypeName(context, type)),
				arguments(visitor, tree, context)));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context, List<AstNode> prev) {
		List<AstNode> js = getObjectInitializer(visitor, tree, context);
		if (js != null) {
			return js;
		}

		js = getInlineFunctionDeclaration(visitor, tree, context);
		if (js != null) {
			return js;
		}

		js = getAnonymousClassDeclaration(visitor, tree, context);
		if (js != null) {
			return js;
		}

		return getRegularNewExpression(visitor, tree, context);

		// if (clazz instanceof ClassWrapper && ClassUtils.isSyntheticType(clazz)) {
		// // this is a call to an mock type
		// printer.print("{}");
		// return;
		// }

	}

}
