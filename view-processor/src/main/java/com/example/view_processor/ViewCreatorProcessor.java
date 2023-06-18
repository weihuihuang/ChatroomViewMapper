package com.example.view_processor;

import com.example.view_annotation.ViewMapperCreatorHost;
import com.google.auto.service.AutoService;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ViewCreatorProcessor extends AbstractProcessor {

    private Messager mMessager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "注解初始化---6666");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("注解初始化---7777");

        Set<? extends Element> classElements = roundEnvironment.getElementsAnnotatedWith(ViewMapperCreatorHost.class);

        for (Element element : classElements) {
            TypeElement classElement = (TypeElement) element;
            ViewCreatorClassGenerator viewCreatorClassGenerator = new ViewCreatorClassGenerator(processingEnv, classElement, mMessager);
            viewCreatorClassGenerator.getJavaClassFile();
            break;
        }
        return true;

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(ViewMapperCreatorHost.class.getCanonicalName());
        return types;
    }

}
