// Copyright (C) 2011 by Michal Zielinski
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package pyjvm;

public class ByteArray extends NativeObj { //!export ByteArray
	public byte[] bytes;
	public int length;
	
	public ByteArray(byte[] bytes, int length) {
		this.bytes = bytes;
		this.length = length;
	}

	public ByteArray(int capacity) {
		this.length = 0;
		this.bytes = new byte[capacity];
	}

	public ByteArray() {
		this(16);
	}
	
	public String toString() {
		return "<bytearray " + repr(this.str()) + ">";
	}
	
	public SString str() {
		return (SString)to_string(0, length);
	}
	
	public Obj to_string(int offset, int length) { //!export
		byte[] dest = new byte[length];
		System.arraycopy(bytes, offset, dest, 0, length);
		return new SString(dest);
	}
	
	public Type getType() {
		return ByteArrayClass.instance;
	}
}