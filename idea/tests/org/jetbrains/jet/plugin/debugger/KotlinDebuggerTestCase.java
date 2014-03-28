/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.plugin.debugger;

import com.intellij.debugger.DebuggerInvocationUtil;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.DebuggerTestCase;
import com.intellij.debugger.engine.evaluation.CodeFragmentKind;
import com.intellij.debugger.engine.evaluation.TextWithImportsImpl;
import com.intellij.debugger.impl.OutputChecker;
import com.intellij.debugger.ui.breakpoints.Breakpoint;
import com.intellij.debugger.ui.breakpoints.BreakpointManager;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.ui.classFilter.ClassFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.MockLibraryUtil;
import org.jetbrains.jet.codegen.forTestCompile.ForTestCompileRuntime;
import org.jetbrains.jet.plugin.PluginTestCaseBase;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public abstract class KotlinDebuggerTestCase extends DebuggerTestCase {
    protected static final String TINY_APP = PluginTestCaseBase.getTestDataPathBase() + "/debugger/tinyApp";

    private File outputDir;

    @Override
    protected OutputChecker initOutputChecker() {
        return new KotlinOutputChecker(TINY_APP);
    }

    @NotNull
    @Override
    protected String getTestAppPath() {
        return TINY_APP;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        if (outputDir != null && outputDir.exists()) {
            FileUtil.delete(outputDir);
        }
    }

    @Override
    protected void ensureCompiledAppExists() throws Exception {
        String modulePath = getTestAppPath();
        outputDir = new File(modulePath + File.separator + "classes");
        MockLibraryUtil.compileKotlin(modulePath + File.separator + "src", outputDir);
    }

    private static class KotlinOutputChecker extends OutputChecker {

        public KotlinOutputChecker(@NotNull String appPath) {
            super(appPath);
        }

        @Override
        protected String replaceAdditionalInOutput(String str) {
            return super.replaceAdditionalInOutput(str.replace(ForTestCompileRuntime.runtimeJarForTests().getPath(), "!KOTLIN_RUNTIME!"));
        }
    }

    /* Copied from super method: ExecutionWithDebuggerToolsTestCase.creteBreakpoint. */
    @Override
    public void createBreakpoints(final PsiFile file) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BreakpointManager breakpointManager = DebuggerManagerEx.getInstanceEx(myProject).getBreakpointManager();
                // Changes in this line: use FileDocumentManager instead of PsiDocumentManager
                Document document = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
                int offset = -1;
                for (; ;) {
                    offset = document.getText().indexOf("Breakpoint!", offset + 1);
                    if (offset == -1) break;

                    int commentLine = document.getLineNumber(offset);

                    String comment = document.getText().substring(document.getLineStartOffset(commentLine), document.getLineEndOffset(commentLine));

                    Breakpoint breakpoint;

                    if (comment.indexOf("Method") != -1) {
                        breakpoint = breakpointManager.addMethodBreakpoint(document, commentLine + 1);
                        println("MethodBreakpoint created at " + file.getVirtualFile().getName() + ":" + (commentLine + 2), ProcessOutputTypes.SYSTEM);
                    }
                    else if (comment.indexOf("Field") != -1) {
                        breakpoint = breakpointManager.addFieldBreakpoint(document, commentLine + 1, readValue(comment, "Field"));
                        println("FieldBreakpoint created at " + file.getVirtualFile().getName() + ":" + (commentLine + 2), ProcessOutputTypes.SYSTEM);
                    }
                    else if (comment.indexOf("Exception") != -1) {
                        breakpoint = breakpointManager.addExceptionBreakpoint(readValue(comment, "Exception"), "");
                        println("ExceptionBreakpoint created at " + file.getVirtualFile().getName() + ":" + (commentLine + 2), ProcessOutputTypes.SYSTEM);
                    }
                    else {
                        breakpoint = breakpointManager.addLineBreakpoint(document, commentLine + 1);
                        println("LineBreakpoint created at " + file.getVirtualFile().getName() + ":" + (commentLine + 2), ProcessOutputTypes.SYSTEM);
                    }

                    String suspendPolicy = readValue(comment, "suspendPolicy");
                    if (suspendPolicy != null) {
                        //breakpoint.setSuspend(!DebuggerSettings.SUSPEND_NONE.equals(suspendPolicy));
                        breakpoint.setSuspendPolicy(suspendPolicy);
                        println("SUSPEND_POLICY = " + suspendPolicy, ProcessOutputTypes.SYSTEM);
                    }
                    String condition = readValue(comment, "Condition");
                    if (condition != null) {
                        //breakpoint.CONDITION_ENABLED = true;
                        breakpoint.setCondition(new TextWithImportsImpl(CodeFragmentKind.EXPRESSION, condition));
                        println("Condition = " + condition, ProcessOutputTypes.SYSTEM);
                    }
                    String passCount = readValue(comment, "Pass count");
                    if (passCount != null) {
                        breakpoint.setCountFilterEnabled(true);
                        breakpoint.setCountFilter(Integer.parseInt(passCount));
                        println("Pass count = " + passCount, ProcessOutputTypes.SYSTEM);
                    }

                    String classFilters = readValue(comment, "Class filters");
                    if (classFilters != null) {
                        breakpoint.setClassFiltersEnabled(true);
                        StringTokenizer tokenizer = new StringTokenizer(classFilters, " ,");
                        ArrayList<ClassFilter> lst = new ArrayList<ClassFilter>();

                        while (tokenizer.hasMoreTokens()) {
                            ClassFilter filter = new ClassFilter();
                            filter.setEnabled(true);
                            filter.setPattern(tokenizer.nextToken());
                            lst.add(filter);
                        }

                        breakpoint.setClassFilters(lst.toArray(new ClassFilter[lst.size()]));
                        println("Class filters = " + classFilters, ProcessOutputTypes.SYSTEM);
                    }
                }
            }
        };
        if (!SwingUtilities.isEventDispatchThread()) {
            DebuggerInvocationUtil.invokeAndWait(myProject, runnable, ModalityState.defaultModalityState());
        }
        else {
            runnable.run();
        }
    }

    @Override
    protected String getAppClassesPath() {
        return super.getAppClassesPath() + File.pathSeparator + ForTestCompileRuntime.runtimeJarForTests().getPath();
    }
}
