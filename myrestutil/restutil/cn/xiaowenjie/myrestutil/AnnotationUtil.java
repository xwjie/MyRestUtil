package cn.xiaowenjie.myrestutil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.*;
import org.apache.commons.lang3.reflect.MethodUtils;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnotationUtil {

	static {
		System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
	}

	/*public static Class<?> addAnnotationToClass(Class<?> clazz, Annotation annotation) {
		Class<?> result = clazz;
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass cc = pool.get(clazz.getCanonicalName());
			ClassFile classFile = cc.getClassFile();
			ConstPool constPool = classFile.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
					AnnotationsAttribute.visibleTag);
			javassist.bytecode.annotation.Annotation javaAssisitAnnotation = createJavaAssistAnnotaion(
					annotation, constPool);
			attr.addAnnotation(javaAssisitAnnotation);
			classFile.addAttribute(attr);
			result = cc.toClass();

		}
		catch (NotFoundException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		catch (CannotCompileException e) {
			e.printStackTrace();
		}
		return result;
	}*/

	public static Class<?> addAnnotationToClass(String canonicalClassName,byte[] classBytes, Annotation annotation)
			throws NotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, CannotCompileException {
		ClassPool pool = ClassPool.getDefault();

		pool.insertClassPath(new ByteArrayClassPath(canonicalClassName, classBytes));
		CtClass ctClass = pool.get(canonicalClassName);
		ClassFile classFile = ctClass.getClassFile();
		ConstPool constPool = classFile.getConstPool();
		AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
				AnnotationsAttribute.visibleTag);
		javassist.bytecode.annotation.Annotation javaAssisitAnnotation = createJavaAssistAnnotaion(
				annotation, constPool);
		attr.addAnnotation(javaAssisitAnnotation);
		classFile.addAttribute(attr);
		Class<?> result = ctClass.toClass();

		return result;
	}

	public static javassist.bytecode.annotation.Annotation createJavaAssistAnnotaion(
			Annotation jdkAnnotation, ConstPool constPool) throws NotFoundException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		CtClass jdkAnnotaionCtClass = ClassPool.getDefault()
				.get(jdkAnnotation.annotationType().getCanonicalName());
		// 使用已有的jdk annotation构造javaassist annotation
		javassist.bytecode.annotation.Annotation javaAssistAnnotaion = new javassist.bytecode.annotation.Annotation(
				constPool, jdkAnnotaionCtClass);
		setJavaAssistAnnotation(javaAssistAnnotaion, jdkAnnotation, constPool);
		return javaAssistAnnotaion;
	}

	private static void setJavaAssistAnnotation(
			javassist.bytecode.annotation.Annotation javaAssistAnnotaion,
			Annotation jdkAnnotation, ConstPool constPool) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Class<? extends Annotation> clazz = jdkAnnotation.annotationType();
		Method[] methods = clazz.getDeclaredMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			log.info("Get method : {}", methodName);
			Object defaultValue = method.getDefaultValue();
			log.info("\tGet Default Value:{}", defaultValue);
			Class<?> returnType = method.getReturnType();
			log.info("\tGet Return Type:{}", returnType);
			Object actualValue = MethodUtils.invokeExactMethod(jdkAnnotation, methodName);
			MemberValue memberValue = createMemberValue(returnType, actualValue,
					defaultValue, constPool);
			if (memberValue != null) {
				javaAssistAnnotaion.addMemberValue(methodName, memberValue);
			}
		}
	}

	private static MemberValue createMemberValue(Class<?> returnType, Object actualValue,
			Object defaultValue, ConstPool constPool) {
		MemberValue result = null;
		if (returnType.isAssignableFrom(String.class)) {
			result = new StringMemberValue(((String) actualValue), constPool);
		}
		else if (returnType.isAssignableFrom(boolean.class)) {
			result = new BooleanMemberValue((Boolean) actualValue, constPool);
		}
		return result;
	}

}
