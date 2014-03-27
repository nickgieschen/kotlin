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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.JetNameReferenceExpression;
import org.jetbrains.jet.lang.psi.stubs.PsiJetNameReferenceExpressionStub;
import org.jetbrains.jet.lang.psi.stubs.impl.PsiJetNameReferenceExpressionStubImpl;

import java.io.IOException;

public class JetNameReferenceExpressionElementType extends JetStubElementType<PsiJetNameReferenceExpressionStub, JetNameReferenceExpression> {
    public JetNameReferenceExpressionElementType(@NotNull @NonNls String debugName) {
        super(debugName);
    }

    @Override
    public JetNameReferenceExpression createPsiFromAst(@NotNull ASTNode node) {
        return new JetNameReferenceExpression(node);
    }

    @Override
    public JetNameReferenceExpression createPsi(@NotNull PsiJetNameReferenceExpressionStub stub) {
         return new JetNameReferenceExpression(stub);
    }

    @Override
    public PsiJetNameReferenceExpressionStub createStub(@NotNull JetNameReferenceExpression psi, StubElement parentStub) {
        return new PsiJetNameReferenceExpressionStubImpl(parentStub);
    }

    @Override
    public void serialize(@NotNull PsiJetNameReferenceExpressionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        //TODO:
    }

    @NotNull
    @Override
    public PsiJetNameReferenceExpressionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new PsiJetNameReferenceExpressionStubImpl(parentStub);
    }

    @Override
    public void indexStub(@NotNull PsiJetNameReferenceExpressionStub stub, @NotNull IndexSink sink) {
        //do nothing
    }
}
