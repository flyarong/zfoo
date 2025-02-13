/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;

/**
 * @author godotg
 * @version 3.0
 */
public class PairString implements IPacket {

    public static final short PROTOCOL_ID = 112;

    private String key;

    private String value;

    public static PairString valueOf(String key, String value) {
        var pair = new PairString();
        pair.key = key;
        pair.value = value;
        return pair;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
