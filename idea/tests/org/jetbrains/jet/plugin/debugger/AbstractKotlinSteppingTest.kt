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

package org.jetbrains.jet.plugin.debugger

import com.intellij.debugger.engine.SuspendContextImpl
import com.intellij.debugger.engine.MethodFilter
import com.intellij.debugger.engine.StackFrameContext
import com.intellij.debugger.engine.DebugProcessImpl
import org.jetbrains.jet.plugin.debugger.KotlinSmartStepIntoHandler.KotlinMethodSmartStepTarget
import com.intellij.debugger.jdi.StackFrameProxyImpl
import com.intellij.debugger.SourcePosition
import com.intellij.psi.PsiElement
import com.intellij.debugger.DebuggerManagerEx
import com.intellij.debugger.ui.breakpoints.LineBreakpoint
import com.intellij.psi.PsiFile
import org.jetbrains.jet.lang.resolve.java.PackageClassUtils
import org.jetbrains.jet.lang.resolve.name.FqName
import java.io.File

abstract class AbstractKotlinSteppingTest : KotlinDebuggerTestCase() {

    protected fun doStepIntoTest(path: String) {
        createDebugProcess(path)
        onBreakpoint { stepInto() }
        finish()
    }

    protected fun doSmartStepIntoTest(path: String) {
        createDebugProcess(path)
        onBreakpoint { smartStepInto() }
        finish()
    }

    // Workaround for KT-4617 - Start
    override fun resume(context: SuspendContextImpl?) = super.resume(context)
    override fun printContext(context: StackFrameContext?) = super.printContext(context)
    override fun printFrameProxy(frameProxy: StackFrameProxyImpl?) = super.printFrameProxy(frameProxy)
    override fun getDebugProcess() = super.getDebugProcess()
    // Workaround for KT-4617 - End

    private val dp: DebugProcessImpl
        get() = getDebugProcess() ?: throw AssertionError("createLocalProcess method should be called before")

    private fun onBreakpoint(f: SuspendContextImpl.() -> Unit) {
        super.onBreakpoint {
            it.printContext()
            it.f()
        }
    }

    private fun SuspendContextImpl.smartStepInto() {
        this.smartStepInto(false)
    }

    private fun SuspendContextImpl.stepInto() {
        this.stepInto(false, null)
    }

    private fun SuspendContextImpl.stepInto(ignoreFilters: Boolean, smartStepFilter: MethodFilter?) {
        dp.getManagerThread()!!.schedule(dp.createStepIntoCommand(this, ignoreFilters, smartStepFilter))
    }

    private fun SuspendContextImpl.smartStepInto(ignoreFilters: Boolean) {
        dp.getManagerThread()!!.schedule(dp.createStepIntoCommand(this, ignoreFilters, createSmartStepIntoFilter()))
    }

    private fun SuspendContextImpl.printContext() {
        printContext(this)
    }

    private fun createDebugProcess(pathToFile: String) {
        val file = File(pathToFile)
        val packageName = file.name.replace(".kt", "")
        createLocalProcess(PackageClassUtils.getPackageClassFqName(FqName(packageName)).asString())
    }

    private fun finish() {
        onBreakpoint {
            resume(this)
        }
    }

    private fun createSmartStepIntoFilter(): KotlinSmartStepIntoHandler.KotlinBasicStepMethodFilter {
        val breakpointManager = DebuggerManagerEx.getInstanceEx(getProject())?.getBreakpointManager()
        val breakpoint = breakpointManager?.getBreakpoints()?.first { it is LineBreakpoint }

        val line = (breakpoint as LineBreakpoint).getLineIndex()

        val containingFile = breakpoint.getPsiFile()
        if (containingFile == null) throw AssertionError("Couldn't find file for breakpoint at the line $line")

        val position = MockSourcePosition(_file = containingFile, _line = line)

        val stepTargets = KotlinSmartStepIntoHandler().findSmartStepTargets(position)
        val firstStepTarget = stepTargets.first()
        if (firstStepTarget !is KotlinMethodSmartStepTarget) throw AssertionError("Couldn't find KotlinMethodSmartStepTarget for line=$line in file=${containingFile.getName()}")

        return KotlinSmartStepIntoHandler.KotlinBasicStepMethodFilter(firstStepTarget)
    }
}
