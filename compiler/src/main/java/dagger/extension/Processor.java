package dagger.extension;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor
{

    private Elements elements;
    private Filer filer;
    private boolean alreadyProcessed = false;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment)
    {
        super.init(processingEnvironment);
        elements = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        return !alreadyProcessed && (Utils.isUnitTest(elements) || Utils.isAndroidTest(elements)) && processElement();
    }

    private boolean processElement()
    {
        TypeElement typeElement = elements.getTypeElement("dagger.TestTrigger");
        if (typeElement == null) {
            return false;
        }
        alreadyProcessed = true;
        ProcessingStep step = new ProcessingStep(filer);
        step.process(typeElement);
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        return ImmutableSet.of("*");
    }

}
