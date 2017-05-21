package dagger.extension;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import dagger.Trigger;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.xml.bind.DatatypeConverter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class ProcessingStep implements BasicAnnotationProcessor.ProcessingStep {

    private Filer filer;

    ProcessingStep(Filer filer) {
        this.filer = filer;
    }

    @Override
    public Set<? extends Class<? extends Annotation>> annotations() {
        return ImmutableSet.of(Trigger.class);
    }

    @Override
    public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> setMultimap) {
        return ImmutableSet.of();
    }

    public void process(TypeElement typeElement)
    {
        final List<ExecutableElement> executableElements = typeElement.getEnclosedElements().stream()
                                                                   .filter(method -> method.getKind() == ElementKind.METHOD &&
                                                                           method.getAnnotation(Trigger.class) != null)
                                                                   .map(element -> (ExecutableElement) element)
                                                                   .collect(Collectors.toList());

        for (ExecutableElement executableElement : executableElements) {
            final Trigger annotation = executableElement.getAnnotation(Trigger.class);
            final String value = annotation.value();
            try {
                byte[] decodedClass = decodeClass(value);
                final JavaFileObject sourceFile = filer.createSourceFile(annotation.qualifiedName());
                final OutputStream os = sourceFile.openOutputStream();
                os.write(decodedClass);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] decodeClass(String value) {
        return DatatypeConverter.parseBase64Binary(value);
    }
}
