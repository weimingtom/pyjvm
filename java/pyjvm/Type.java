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

public abstract class Type extends Obj { //!export Type
	public static final class EmptyType extends Type {
		private String name;

		public EmptyType(String name) {
			this.name = name;
		}

		public String toString() {
			return "<type '" + this.name + "'>";
		}

		public Obj getEntry(int name) {
			throw new ScriptError(ScriptError.TypeError, "Class without attributes");
		}

	}

	public abstract Obj getEntry(int name);

	public Type[] getBases() {
		if(this == NativeObjClass.instance)
			return new Type[0];
		else
			return DEFUALT_BASES;
	}

	public Type getType() {
		return TypeClass.instance;
	}

	public SBool isEqual(Obj other) {
		return this == other ? SBool.True: SBool.False;
	}

	public static Type[] DEFUALT_BASES = new Type[]{
		NativeObjClass.instance
	};
}
