package cn.xiaowenjie.myrestutil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.lang.annotation.Annotation;

public class AnnotationUtil {
	public static Class<?> addAnnotationToClass(Class<?> clazz, Annotation annotation) {
		Class<?> result = clazz;
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass cc = pool.get(clazz.getCanonicalName());
			ClassFile classFile = cc.getClassFile();
			ConstPool constPool = classFile.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
					AnnotationsAttribute.visibleTag);

		}
		catch (NotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static javassist.bytecode.annotation.Annotation createJavaAssistAnnotaion(
			Annotation jdkAnnotation, ConstPool constPool) throws NotFoundException {
		Class<? extends Annotation> clazz = jdkAnnotation.annotationType();
		String annotationName = clazz.getSimpleName();

		CtClass jdkAnnotaionCtClass = ClassPool.getDefault()
				.get(jdkAnnotation.annotationType().getCanonicalName());

		javassist.bytecode.annotation.Annotation javaAssistAnnotaion = new javassist.bytecode.annotation.Annotation(
				constPool, jdkAnnotaionCtClass);
		// TODO: TOBE
		return javaAssistAnnotaion;
	}

}
