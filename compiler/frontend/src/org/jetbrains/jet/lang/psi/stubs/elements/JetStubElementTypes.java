/*
 * Copyright 2010-2013 JetBrains s.r.o.
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

public interface JetStubElementTypes {
    JetFileElementType FILE = new JetFileElementType();

    JetClassElementType CLASS = new JetClassElementType("CLASS");
    JetFunctionElementType FUNCTION = new JetFunctionElementType("FUN");
    JetPropertyElementType PROPERTY = new JetPropertyElementType("PROPERTY");
    JetClassElementType ENUM_ENTRY = new JetClassElementType("ENUM_ENTRY");
    JetObjectElementType OBJECT_DECLARATION = new JetObjectElementType("OBJECT_DECLARATION");
    JetClassObjectElementType CLASS_OBJECT = new JetClassObjectElementType("CLASS_OBJECT");

    JetParameterElementType VALUE_PARAMETER = new JetParameterElementType("VALUE_PARAMETER");
    JetParameterListElementType VALUE_PARAMETER_LIST = new JetParameterListElementType("VALUE_PARAMETER_LIST");

    JetTypeParameterElementType TYPE_PARAMETER = new JetTypeParameterElementType("TYPE_PARAMETER");
    JetTypeParameterListElementType TYPE_PARAMETER_LIST = new JetTypeParameterListElementType("TYPE_PARAMETER_LIST");

    JetAnnotationEntryElementType ANNOTATION_ENTRY = new JetAnnotationEntryElementType("ANNOTATION_ENTRY");
    JetAnnotationElementType ANNOTATION = new JetAnnotationElementType("ANNOTATION");

    JetClassBodyElementType CLASS_BODY = new JetClassBodyElementType("CLASS_BODY");
    JetImportListElementType IMPORT_LIST = new JetImportListElementType("IMPORT_LIST");
    JetImportDirectiveElementType IMPORT_DIRECTIVE = new JetImportDirectiveElementType("IMPORT_DIRECTIVE");

    JetModifierListElementType MODIFIER_LIST = new JetModifierListElementType("MODIFIER_LIST");
    JetModifierListElementType PRIMARY_CONSTRUCTOR_MODIFIER_LIST = new JetModifierListElementType("PRIMARY_CONSTRUCTOR_MODIFIER_LIST");

    JetTypeConstraintListElementType TYPE_CONSTRAINT_LIST = new JetTypeConstraintListElementType("TYPE_CONSTRAINT_LIST");

    JetNullableTypeElementType NULLABLE_TYPE = new JetNullableTypeElementType("NULLABLE_TYPE");
    JetTypeReferenceElementType TYPE_REFERENCE = new JetTypeReferenceElementType("TYPE_REFERENCE");
    JetUserTypeElementType USER_TYPE = new JetUserTypeElementType("USER_TYPE");
    JetFunctionTypeElementType FUNCTION_TYPE = new JetFunctionTypeElementType("FUNCTION_TYPE");

    JetNameReferenceExpressionElementType REFERENCE_EXPRESSION = new JetNameReferenceExpressionElementType("REFERENCE_EXPRESSION");
    JetDotQualifiedExpressionElementType DOT_QUALIFIED_EXPRESSION = new JetDotQualifiedExpressionElementType("DOT_QUALIFIED_EXPRESSION");

    JetTypeArgumentListElementType TYPE_ARGUMENT_LIST = new JetTypeArgumentListElementType("TYPE_ARGUMENT_LIST");

    JetDelegationSpecifierListElementType DELEGATION_SPECIFIER_LIST = new JetDelegationSpecifierListElementType("DELEGATION_SPECIFIER_LIST");
}
