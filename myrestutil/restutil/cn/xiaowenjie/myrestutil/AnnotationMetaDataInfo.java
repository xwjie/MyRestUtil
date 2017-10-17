package cn.xiaowenjie.myrestutil;

import lombok.*;

import java.lang.annotation.Annotation;
import java.util.List;

@Builder
@Getter
public class AnnotationMetaDataInfo {
	private String newClassName;
	private List<Annotation> classAnnotations;
	private List<MethodAnnotation> methodAnnotations;

	@Setter
	@Getter
	@ToString
	@AllArgsConstructor
	public static class MethodAnnotation {
		private String methodName;
		private List<Annotation> annotations;

	}
}
