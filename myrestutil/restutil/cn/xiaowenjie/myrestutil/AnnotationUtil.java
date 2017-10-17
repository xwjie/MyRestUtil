package cn.xiaowenjie.myrestutil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.commons.lang3.reflect.MethodUtils;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 李佳明
 * @date 2017.10.16
 */
@Slf4j
public class AnnotationUtil {

	/*
	 * static { System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
	 * }
	 */

	/**
	 * 运行时动态修改Class字节码，添加Class级别的注解
	 *
	 * @param canonicalClassName 添加注解后的Class名
	 * @param classBytes 修改前的字节码
	 * @param annotation 要添加的注解实例
	 * @return
	 * @throws NotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws CannotCompileException
	 */
	public static Class<?> addAnnotationToClass(String canonicalClassName,
			byte[] classBytes, Annotation annotation)
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

	/**
	 * 根据JDK的注解实例，创建javaassist的注解实例
	 * @param jdkAnnotation
	 * @param constPool
	 * @return
	 * @throws NotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
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

	/**
	 * 遍历jdk注解属性，设置到javaassist注解中
	 * @param javaAssistAnnotaion
	 * @param jdkAnnotation
	 * @param constPool
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static void setJavaAssistAnnotation(
			javassist.bytecode.annotation.Annotation javaAssistAnnotaion,
			Annotation jdkAnnotation, ConstPool constPool) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {

		Class<? extends Annotation> clazz = jdkAnnotation.annotationType();
		Method[] methods = clazz.getDeclaredMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			log.info("Get method : {}", methodName);
			Object value = method.getDefaultValue();
			log.info("\tGet Default Value:{}", value);
			Class<?> returnType = method.getReturnType();
			log.info("\tGet Return Type:{}", returnType);
			Object actualValue = MethodUtils.invokeExactMethod(jdkAnnotation, methodName);
			if (!Objects.equals(value, actualValue)) {
				value = actualValue;
			}
			MemberValue memberValue = createMemberValue(returnType, value, constPool);
			if (memberValue != null) {
				javaAssistAnnotaion.addMemberValue(methodName, memberValue);
			}
		}
	}

	/**
	 * 工程方法，根据returnType类型创建相应的MemberValue
	 * @param returnType
	 * @param value
	 * @param constPool
	 * @return
	 */
	private static MemberValue createMemberValue(Class<?> returnType, Object value,
			ConstPool constPool) {
		MemberValue result = null;
		if (returnType.isAssignableFrom(String.class)) {
			result = new StringMemberValue(((String) value), constPool);
		}
		else if (returnType.isAssignableFrom(boolean.class)) {
			result = new BooleanMemberValue((Boolean) value, constPool);
		}
		// TODO: 编写其他类型的
		return result;
	}

}
