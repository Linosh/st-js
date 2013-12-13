package org.stjs.generator.check;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.declaration.ArrayTypeForbiddenCheck;
import org.stjs.generator.check.declaration.ClassDuplicateMemberNameCheck;
import org.stjs.generator.check.declaration.ClassEnumWithoutMembersCheck;
import org.stjs.generator.check.declaration.ClassForbidExtendsSyntheticTypeCheck;
import org.stjs.generator.check.declaration.ClassGlobalInstanceMembersCheck;
import org.stjs.generator.check.declaration.ClassImplementJavascriptFunctionCheck;
import org.stjs.generator.check.declaration.ClassNamespaceCheck;
import org.stjs.generator.check.declaration.FieldInitializerCheck;
import org.stjs.generator.check.declaration.MethodOverloadCheck;
import org.stjs.generator.check.declaration.MethodSynchronizedCheck;
import org.stjs.generator.check.declaration.MethodVarArgParamCheck;
import org.stjs.generator.check.expression.IdentifierAccessOuterScopeCheck;
import org.stjs.generator.check.expression.IdentifierGlobalScopeNameClashCheck;
import org.stjs.generator.check.expression.MemberSelectGlobalScopeNameClashCheck;
import org.stjs.generator.check.expression.MemberSelectOuterScopeCheck;
import org.stjs.generator.check.expression.MethodInvocationMapConstructorCheck;
import org.stjs.generator.check.expression.MethodInvocationOuterScopeCheck;
import org.stjs.generator.check.expression.NewArrayForbiddenCheck;
import org.stjs.generator.check.expression.NewClassInlineFunctionCheck;
import org.stjs.generator.check.expression.NewClassObjectInitCheck;
import org.stjs.generator.check.statement.AssertCheck;
import org.stjs.generator.check.statement.BlockInstanceCheck;
import org.stjs.generator.check.statement.SynchronizedCheck;
import org.stjs.generator.check.statement.VariableFinalInLoopCheck;
import org.stjs.generator.check.statement.VariableWrongNameCheck;
import org.stjs.generator.visitor.TreePathScannerContributors;

public class CheckContributors {
	public static void addContributors(TreePathScannerContributors<Void, GenerationContext> contributors) {
		contributors.contribute(new VariableFinalInLoopCheck());
		contributors.contribute(new VariableWrongNameCheck());
		contributors.contribute(new MethodVarArgParamCheck());
		contributors.contribute(new FieldInitializerCheck());
		contributors.contribute(new ClassDuplicateMemberNameCheck());
		contributors.contribute(new IdentifierAccessOuterScopeCheck());
		contributors.contribute(new NewClassInlineFunctionCheck());
		contributors.contribute(new ClassImplementJavascriptFunctionCheck());
		contributors.contribute(new ClassGlobalInstanceMembersCheck());
		contributors.contribute(new ClassNamespaceCheck());
		contributors.contribute(new MethodOverloadCheck());
		contributors.contribute(new NewClassObjectInitCheck());
		contributors.contribute(new ClassForbidExtendsSyntheticTypeCheck());
		contributors.contribute(new MethodInvocationOuterScopeCheck());
		contributors.contribute(new MemberSelectOuterScopeCheck());
		contributors.contribute(new ArrayTypeForbiddenCheck());
		contributors.contribute(new ClassEnumWithoutMembersCheck());
		contributors.contribute(new NewArrayForbiddenCheck());
		contributors.contribute(new BlockInstanceCheck());
		contributors.contribute(new MethodInvocationMapConstructorCheck());
		contributors.contribute(new SynchronizedCheck());
		contributors.contribute(new MethodSynchronizedCheck());
		contributors.contribute(new AssertCheck());

		contributors.contribute(new IdentifierGlobalScopeNameClashCheck());
		contributors.contribute(new MemberSelectGlobalScopeNameClashCheck());
	}
}