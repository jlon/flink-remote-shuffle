/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.flink.shuffle.core.memory;

import com.alibaba.flink.shuffle.common.utils.CommonUtils;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/** Tests for {@link Buffer}. */
public class BufferTest {

    @Test
    public void testRecycle() {
        int numBuffers = 100;
        int bufferSize = 1024;
        TestBufferRecycler recycler = new TestBufferRecycler();
        for (int i = 0; i < numBuffers; ++i) {
            Buffer buffer = createBuffer(bufferSize, 0, recycler);
            buffer.release();
        }
        assertEquals(numBuffers, recycler.getNumRecycledBuffers());
    }

    @Test
    public void testReadableBytes() {
        int bufferSize = 1024;
        int readableBytes = 512;
        Buffer buffer = createBuffer(bufferSize, readableBytes, new TestBufferRecycler());
        assertEquals(readableBytes, buffer.readableBytes());
    }

    private Buffer createBuffer(int bufferSize, int readableBytes, BufferRecycler recycler) {
        CommonUtils.checkArgument(bufferSize >= readableBytes);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferSize);
        return new Buffer(byteBuffer, recycler, readableBytes);
    }
}