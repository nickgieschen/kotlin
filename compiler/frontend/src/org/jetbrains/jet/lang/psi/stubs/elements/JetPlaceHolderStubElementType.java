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

package org.jetbrains.jet.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ReflectionUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.JetElement;
import org.jetbrains.jet.lang.psi.stubs.PsiJetPlaceHolderStub;
import org.jetbrains.jet.lang.psi.stubs.impl.PsiJetPlaceHolderStubImpl;

import java.io.IOException;
import java.lang.reflect.Constructor;

public abstract class JetPlaceHolderStubElementType<T extends JetElement> extends JetStubElementType<PsiJetPlaceHolderStub<T>, T> {

    @NotNull
    private final Constructor<T> byNodeConstructor;
    @NotNull
    private final Constructor<T> byStubConstructor;

    public JetPlaceHolderStubElementType(@NotNull @NonNls String debugName, @NotNull Class<T> clazz) {
        super(debugName);
        try {
            byNodeConstructor = clazz.getConstructor(ASTNode.class);
            byStubConstructor = clazz.getConstructor(PsiJetPlaceHolderStub.class);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("Stub element type declaration for " + clazz.getSimpleName() + " is missing required constructors",e);
        }
    }

    @Override
    public T createPsiFromAst(@NotNull ASTNode node) {
        return ReflectionUtil.createInstance(byNodeConstructor, node);
    }

    @Override
    public T createPsi(@NotNull PsiJetPlaceHolderStub<T> stub) {
        return ReflectionUtil.createInstance(byStubConstructor, stub);
    }

    @Override
    public PsiJetPlaceHolderStub<T> createStub(@NotNull T psi, StubElement parentStub) {
        return new PsiJetPlaceHolderStubImpl<T>(parentStub, getInstance());
    }

    @Override
    public void serialize(@NotNull PsiJetPlaceHolderStub<T> stub, @NotNull StubOutputStream dataStream) throws IOException {
        //do nothing
    }

    @NotNull
    @Override
    public PsiJetPlaceHolderStub<T> deserialize(
            @NotNull StubInputStream dataStream, StubElement parentStub
    ) throws IOException {
        return new PsiJetPlaceHolderStubImpl<T>(parentStub, getInstance());
    }

    @Override
    public void indexStub(@NotNull PsiJetPlaceHolderStub<T> stub, @NotNull IndexSink sink) {
        //do nothing
    }

    @NotNull
    protected abstract JetPlaceHolderStubElementType<T> getInstance();
}
