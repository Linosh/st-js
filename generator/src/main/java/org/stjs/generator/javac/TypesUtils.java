package org.stjs.generator.javac;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.sun.tools.javac.model.JavacTypes;

/**
 * A utility class that helps with {@link TypeMirror}s.
 * 
 */
// TODO: This class needs significant restructuring
@edu.umd.cs.findbugs.annotations.SuppressWarnings(
		justification = "copied code", value = "BC_UNCONFIRMED_CAST")
@SuppressWarnings("PMD")
// CHECKSTYLE:OFF
public final class TypesUtils {

	// Class cannot be instantiated
	private TypesUtils() {
		throw new AssertionError("Class TypesUtils cannot be instantiated.");
	}

	/**
	 * Gets the fully qualified name for a provided type. It returns an empty name if type is an anonymous type.
	 * 
	 * @param type
	 *            the declared type
	 * @return the name corresponding to that type
	 */
	public static Name getQualifiedName(DeclaredType type) {
		TypeElement element = (TypeElement) type.asElement();
		return element.getQualifiedName();
	}

	/**
	 * Checks if the type represents a java.lang.Object declared type.
	 * 
	 * @param type
	 *            the type
	 * @return true iff type represents java.lang.Object
	 */
	public static boolean isObject(TypeMirror type) {
		return isDeclaredOfName(type, "java.lang.Object");
	}

	/**
	 * Checks if the type represents a java.lang.Class declared type.
	 * 
	 * @param type
	 *            the type
	 * @return true iff type represents java.lang.Class
	 */
	public static boolean isClass(TypeMirror type) {
		return isDeclaredOfName(type, "java.lang.Class");
	}

	/**
	 * Checks if the type represents a java.lang.String declared type. TODO: it would be cleaner to use
	 * String.class.getCanonicalName(), but the two existing methods above don't do that, I guess for performance
	 * reasons.
	 * 
	 * @param type
	 *            the type
	 * @return true iff type represents java.lang.String
	 */
	public static boolean isString(TypeMirror type) {
		return isDeclaredOfName(type, "java.lang.String");
	}

	/**
	 * Checks if the type represents a boolean type, that is either boolean (primitive type) or java.lang.Boolean.
	 * 
	 * @param type
	 *            the type to test
	 * @return true iff type represents a boolean type
	 */
	public static boolean isBooleanType(TypeMirror type) {
		return isDeclaredOfName(type, "java.lang.Boolean") || type.getKind().equals(TypeKind.BOOLEAN);
	}

	/**
	 * Check if the type represent a declared type of the given qualified name
	 * 
	 * @param type
	 *            the type
	 * @return type iff type represents a declared type of the qualified name
	 */
	public static boolean isDeclaredOfName(TypeMirror type, CharSequence qualifiedName) {
		// type = ((com.sun.tools.javac.code.Type)type).unannotatedType();

		return type.getKind() == TypeKind.DECLARED && getQualifiedName((DeclaredType) type).contentEquals(qualifiedName);

	}

	public static boolean isBoxedPrimitive(TypeMirror type) {
		if (type.getKind() != TypeKind.DECLARED) {
			return false;
		}

		String qualifiedName = getQualifiedName((DeclaredType) type).toString();

		return (qualifiedName.equals("java.lang.Boolean") || qualifiedName.equals("java.lang.Byte")
				|| qualifiedName.equals("java.lang.Character") || qualifiedName.equals("java.lang.Short")
				|| qualifiedName.equals("java.lang.Integer") || qualifiedName.equals("java.lang.Long")
				|| qualifiedName.equals("java.lang.Double") || qualifiedName.equals("java.lang.Float"));
	}

	/** @return type represents a Throwable type (e.g. Exception, Error) **/
	public static boolean isThrowable(TypeMirror type) {
		while (type != null && type.getKind() == TypeKind.DECLARED) {
			DeclaredType dt = (DeclaredType) type;
			TypeElement elem = (TypeElement) dt.asElement();
			Name name = elem.getQualifiedName();
			if ("java.lang.Throwable".contentEquals(name)) {
				return true;
			}
			type = elem.getSuperclass();
		}
		return false;
	}

	/**
	 * Returns true iff the argument is a primitive type.
	 * 
	 * @return whether the argument is a primitive type
	 */
	public static boolean isPrimitive(TypeMirror type) {
		// type = ((com.sun.tools.javac.code.Type) type).unannotatedType();
		switch (type.getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns true iff the arguments are both the same primitive types.
	 * 
	 * @return whether the arguments are the same primitive types
	 */
	public static boolean areSamePrimitiveTypes(TypeMirror left, TypeMirror right) {
		if (!isPrimitive(left) || !isPrimitive(right)) {
			return false;
		}

		return (left.getKind() == right.getKind());
	}

	/**
	 * Returns true iff the argument is a primitive numeric type.
	 * 
	 * @return whether the argument is a primitive numeric type
	 */
	public static boolean isNumeric(TypeMirror type) {
		// type = ((com.sun.tools.javac.code.Type) type).unannotatedType();
		switch (type.getKind()) {
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns true iff the argument is an integral type.
	 * 
	 * @return whether the argument is an integral type
	 */
	public static boolean isIntegral(TypeMirror type) {
		// type = ((com.sun.tools.javac.code.Type) type).unannotatedType();
		switch (type.getKind()) {
		case BYTE:
		case CHAR:
		case INT:
		case LONG:
		case SHORT:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns true iff the argument is a floating point type.
	 * 
	 * @return whether the argument is a floating point type
	 */
	public static boolean isFloating(TypeMirror type) {
		// type = ((com.sun.tools.javac.code.Type) type).unannotatedType();
		switch (type.getKind()) {
		case DOUBLE:
		case FLOAT:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns the widened numeric type for an arithmetic operation performed on a value of the left type and the right
	 * type. Defined in JLS 5.6.2. We return a {@link TypeKind} because creating a {@link TypeMirror} requires a
	 * {@link Types} object from the {@link javax.annotation.processing.ProcessingEnvironment}.
	 * 
	 * @return the result of widening numeric conversion, or NONE when the conversion cannot be performed
	 */
	public static TypeKind widenedNumericType(TypeMirror left, TypeMirror right) {
		if (!isNumeric(left) || !isNumeric(right)) {
			return TypeKind.NONE;
		}

		TypeKind leftKind = left.getKind();
		TypeKind rightKind = right.getKind();

		if (leftKind == TypeKind.DOUBLE || rightKind == TypeKind.DOUBLE) {
			return TypeKind.DOUBLE;
		}

		if (leftKind == TypeKind.FLOAT || rightKind == TypeKind.FLOAT) {
			return TypeKind.FLOAT;
		}

		if (leftKind == TypeKind.LONG || rightKind == TypeKind.LONG) {
			return TypeKind.LONG;
		}

		return TypeKind.INT;
	}

	/**
	 * If the argument is a bounded TypeVariable or WildcardType, return its non-variable, non-wildcard upper bound.
	 * Otherwise, return the type itself.
	 * 
	 * @param type
	 *            a type
	 * @return the non-variable, non-wildcard upper bound of a type, if it has one, or itself if it has no bounds
	 */
	public static TypeMirror upperBound(TypeMirror type) {
		do {
			if (type instanceof TypeVariable) {
				TypeVariable tvar = (TypeVariable) type;
				if (tvar.getUpperBound() != null) {
					type = tvar.getUpperBound();
				} else {
					break;
				}
			} else if (type instanceof WildcardType) {
				WildcardType wc = (WildcardType) type;
				if (wc.getExtendsBound() != null) {
					type = wc.getExtendsBound();
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
		return type;
	}

	/**
	 * Returns the {@link TypeMirror} for a given {@link Class}.
	 */
	public static TypeMirror typeFromClass(Types types, Elements elements, Class<?> clazz) {
		if (clazz == void.class) {
			return types.getNoType(TypeKind.VOID);
		} else if (clazz.isPrimitive()) {
			String primitiveName = clazz.getName().toUpperCase();
			TypeKind primitiveKind = TypeKind.valueOf(primitiveName);
			return types.getPrimitiveType(primitiveKind);
		} else if (clazz.isArray()) {
			TypeMirror componentType = typeFromClass(types, elements, clazz.getComponentType());
			return types.getArrayType(componentType);
		} else {
			TypeElement element = elements.getTypeElement(clazz.getCanonicalName());
			if (element == null) {
				ErrorReporter.errorAbort("Unrecognized class: " + clazz);
				return null; // dead code
			}
			return element.asType();
		}
	}

	/**
	 * Returns an {@link ArrayType} with elements of type {@code componentType}.
	 */
	public static ArrayType createArrayType(Types types, TypeMirror componentType) {
		JavacTypes t = (JavacTypes) types;
		return t.getArrayType(componentType);
	}
}
// CHECKSTYLE:ON