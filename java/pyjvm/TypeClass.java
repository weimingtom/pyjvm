// AUTOGENERATED by exporter.py from Type.java
package pyjvm;

import pyjvm.*;

public final class TypeClass extends Type {
	private TypeClass() {}

	public static final StringDict dict;
	public static final TypeClass instance = new TypeClass();
	public static final Obj constructor = null;
	
	static {
		if("Type".equals("NativeObj") || "Type".equals("UserObj"))
			dict = new StringDict();
		else
			dict = NativeObjClass.dict.copy();
		
	}
	public final StringDict getDict() {
		return dict;
	}
	public final Obj getEntry(int name) {
		return dict.get(name);
	}
	public final Obj call(Obj[] args) {
		if(constructor == null)
			throw new ScriptError(ScriptError.TypeError, "Object uninitializable");
		return constructor.call(args);
	}
}