/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.config.xml;

import com.consol.citrus.functions.*;
import com.consol.citrus.functions.core.*;
import com.consol.citrus.testng.AbstractBeanDefinitionParserTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author Christoph Deppisch
 * @since 2.0
 */
public class FunctionLibraryParserTest extends AbstractBeanDefinitionParserTest {

    @BeforeClass
    @Override
    protected void parseBeanDefinitions() {
    }

    @Test
    public void testNamespaceContextParser() throws Exception {
        beanDefinitionContext = createApplicationContext("context");
        Map<String, FunctionLibrary> functionLibraries = beanDefinitionContext.getBeansOfType(FunctionLibrary.class);

        Assert.assertEquals(functionLibraries.size(), 2L);

        FunctionLibrary functionLibraryBean = functionLibraries.get("functionLib");
        Assert.assertEquals(functionLibraryBean.getMembers().size(), 3L);
        Assert.assertEquals(functionLibraryBean.getMembers().get("randomNr").getClass(), RandomNumberFunction.class);
        Assert.assertEquals(functionLibraryBean.getMembers().get("randomStr").getClass(), RandomStringFunction.class);
        Assert.assertEquals(functionLibraryBean.getMembers().get("custom").getClass(), CustomFunction.class);

        Assert.assertEquals(functionLibraryBean.getMembers().get("custom").execute(FunctionParameterHelper.getParameterList("Christoph"), context), "Hello Christoph!");

        functionLibraryBean = functionLibraries.get("functionLib2");
        Assert.assertEquals(functionLibraryBean.getMembers().size(), 2L);
        Assert.assertEquals(functionLibraryBean.getMembers().get("concat").getClass(), ConcatFunction.class);
        Assert.assertEquals(functionLibraryBean.getMembers().get("custom").getClass(), CustomFunction.class);

        Assert.assertEquals(functionLibraryBean.getMembers().get("custom").execute(FunctionParameterHelper.getParameterList("Citrus"), context), "Hello Citrus!");
    }
}
