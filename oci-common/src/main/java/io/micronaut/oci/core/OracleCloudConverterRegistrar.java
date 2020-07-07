/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.oci.core;

import com.oracle.bmc.Region;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.convert.TypeConverterRegistrar;

import javax.inject.Singleton;

/**
 * Converters for Oracle Cloud Configuration.
 */
@Singleton
public class OracleCloudConverterRegistrar implements TypeConverterRegistrar {
    @Override
    public void register(ConversionService<?> conversionService) {
        conversionService.addConverter(String.class, Region.class, Region::fromRegionCodeOrId);
    }
}