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

package org.jetbrains.jet.lang.descriptors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.annotations.Annotated;
import org.jetbrains.jet.lang.resolve.name.Name;
import org.jetbrains.jet.lang.resolve.scopes.JetScope;

public interface ScriptDescriptor extends Annotated, DeclarationDescriptor, DeclarationDescriptorNonRoot {
    String LAST_EXPRESSION_VALUE_FIELD_NAME = "rv";
    Name NAME = Name.special("<script>");

    int getPriority();

    @NotNull
    FunctionDescriptor getScriptCodeDescriptor();

    @NotNull
    ReceiverParameterDescriptor getThisAsReceiverParameter();

    @NotNull
    ClassDescriptor getClassDescriptor();

    @NotNull
    JetScope getScopeForBodyResolution();
}
