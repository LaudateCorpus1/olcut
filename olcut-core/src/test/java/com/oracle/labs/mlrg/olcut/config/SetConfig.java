/*
 * Copyright (c) 2004-2020, Oracle and/or its affiliates.
 *
 * Licensed under the 2-clause BSD license.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.oracle.labs.mlrg.olcut.config;

import java.util.Set;

/**
 *
 */
public class SetConfig implements Configurable {

    @Config
    public Set<String> stringSet;

    @Config
    public Set<Double> doubleSet;

    @Config
    public Set<StringConfigurable> stringConfigurableSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetConfig setConfig = (SetConfig) o;

        if (stringSet != null ? !stringSet.equals(setConfig.stringSet) : setConfig.stringSet != null) return false;
        if (doubleSet != null ? !doubleSet.equals(setConfig.doubleSet) : setConfig.doubleSet != null) return false;
        return stringConfigurableSet != null ? stringConfigurableSet.equals(setConfig.stringConfigurableSet) : setConfig.stringConfigurableSet == null;
    }

    @Override
    public int hashCode() {
        int result = stringSet != null ? stringSet.hashCode() : 0;
        result = 31 * result + (doubleSet != null ? doubleSet.hashCode() : 0);
        result = 31 * result + (stringConfigurableSet != null ? stringConfigurableSet.hashCode() : 0);
        return result;
    }
}
