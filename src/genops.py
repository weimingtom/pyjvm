# Copyright (C) 2011 by Michal Zielinski
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
import os
os.chdir(os.path.join(os.path.dirname(__file__), '..', 'java'))

import sys
sys.stdout = open('pyjvm/BinOpInstrs.java', 'w')

names = ['add', 'sub', 'mul', 'floordiv', 'div', 'truediv', 'mod']
names = [ (n, n, 'r' + n) for n in names ]

names += [('==', 'isEqual', 'isEqual'),
		('<', 'lessThan', 'greaterOrEqual'),
		('>', 'greaterThan', 'lessOrEqual'),
		('>=', 'greaterOrEqual', 'greaterOrEqual'),
		('<=', 'lessOrEqual', 'greaterThan')]
names += [
	(symbol, 'i' + method, 'r' + method)
	for symbol, method in [
		('+=', 'add'), ('-=', 'sub')
	]
]

print '''// AUTOGENERATED by genops.py
package pyjvm;
public final class BinOpInstrs {'''

def capitalize(str):
	return str[0].upper() + str[1:]

for op, name, symmetric in names:
	cname = capitalize(name)
	s = '''
	public static final class %(cname)s extends GenericInstrs.BinOp {
		public Instr run(Frame frame) {
			Obj a = frame.reg[inreg0];
			Obj b = frame.reg[inreg1];
			Obj result = a.%(name)s(frame, b);
			if(result == NotImplemented) {
				result = b.%(symmetric)s(frame, a);
				if(result == NotImplemented)
					this.operatorFailed(a, b, "%(op)s");
			}
			frame.reg[outreg0] = result;
			return next;
		}
	}''' % locals()
	if name == 'isEqual':
		s = s.replace('NotImplemented', 'null')
	print s

print '''
	public static abstract class BinOpFactory extends Obj {
		public abstract GenericInstrs.BinOp create();
	}
	
	public static final StringDict binOpTypes = new StringDict();
	
	static {'''

for op, name, symmetric in names:
	cname = capitalize(name)
	print '''
		binOpTypes.put("%(op)s", new BinOpFactory(){
			public GenericInstrs.BinOp create() {
				return new %(cname)s();
			}
		});''' % locals()

print '''
	}
}'''
sys.stdout.close()