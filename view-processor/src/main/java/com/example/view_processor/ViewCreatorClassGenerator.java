package com.example.view_processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class ViewCreatorClassGenerator {

    private TypeElement mTypeElement;

    private String mPackageName;
    private String mClassName;
    private ProcessingEnvironment mProcessingEnv;
    private Messager mMessager;

    private static final String sProxyInterfaceName = "IViewCreator";


    public ViewCreatorClassGenerator(ProcessingEnvironment processingEnv, TypeElement classElement, Messager messager) {
        mProcessingEnv = processingEnv;
        mMessager = messager;
        mTypeElement = classElement;
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = ClassValidator.getClassName(classElement, packageName);

        mPackageName = packageName;
        mClassName = className + "__ViewCreator__Proxy";
    }

    public void getJavaClassFile() {

        Writer writer = null;
        try {
            JavaFileObject jfo = mProcessingEnv.getFiler().createSourceFile(
                    mClassName,
                    mTypeElement);

            String classPath = jfo.toUri().getPath();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "jfo-------- : " + classPath + "\n");
            String buildDirStr = "/chatroom/build/";
            String buildDirFullPath = classPath.substring(0, classPath.indexOf(buildDirStr) + buildDirStr.length());
            mMessager.printMessage(Diagnostic.Kind.NOTE, "jfo-------- : " + buildDirFullPath + "\n");
            File customViewFile = new File(buildDirFullPath + "chatroom_view/chatroom_layout_views.txt");
            HashSet<String> customViewClassNameSet = new HashSet<>();
            putClassListData(customViewClassNameSet, customViewFile);
            String generateClassInfoStr = generateClassInfoStr(customViewClassNameSet);
            writer = jfo.openWriter();
            writer.write(generateClassInfoStr);
            writer.flush();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "generate file path : " + classPath +"\n");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private String generateClassInfoStr(HashSet<String> customViewClassNameSet) {

        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import com.ypp.chatroom.*;\n");
        builder.append("import android.content.Context;\n");
        builder.append("import android.util.AttributeSet;\n");
        builder.append("import android.view.*;\n");
        builder.append("import android.widget.*;\n");
        builder.append("import android.webkit.*;\n");
        builder.append("import android.app.*;\n");



        builder.append('\n');

        builder.append("public class ").append(mClassName).append(" implements " + sProxyInterfaceName);
        builder.append(" {\n");

        generateMethodStr(builder, customViewClassNameSet);
        builder.append('\n');

        builder.append("}\n");
        return builder.toString();

    }

    private void generateMethodStr(StringBuilder builder, HashSet<String> customViewClassNameSet) {

        builder.append("@Override\n ");
        builder.append("public View createView(String name, Context context, AttributeSet attrs ) {\n");


        builder.append("switch(name)");
        builder.append("{\n"); // switch start

        for (String className : customViewClassNameSet) {
            if (className == null || className.trim().length() == 0) {
                continue;
            }
            //处理一下
            if(className.contains("HostRefreshHeader")
                    || className.contains("com.github.chrisbanes.photoview.PhotoView")
                    || className.contains("sud")
                    || className.contains("SingleLineEditText")
                    || className.contains("CropIwaView")
                    || className.contains("SmartRefreshLayout")
                    || className.contains("MaterialCalendarGridView")
                    || className.contains("GameroomSeatView")
                    || className.contains("GameroomSeatCardView")
                    || className.contains("androidx.appcompat.widget.CardView")
                    || className.contains("gameroom.widget.GameroomCardEditChoiceView")
            ) {
                continue;
            }




            builder.append("case \"" + className + "\" :\n");
            builder.append("return new " + className + "(context,attrs);\n");
        }

        builder.append("}\n"); //switch end

        builder.append("return null;\n");
        builder.append("  }\n"); // method end

    }

    private void putClassListData(HashSet<String> customViewClassNameSet, File customViewFile) {
        if (customViewFile.exists()) {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(customViewFile);
                br = new BufferedReader(fr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.trim().length() <= 0) {
                        continue;
                    }
                    customViewClassNameSet.add(line.trim());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    // ignore
                }
                try {
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

}
