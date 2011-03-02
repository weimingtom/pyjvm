// AUTOGENERATED by exporter.py from SString.java
package pyjvm;

public final class SStringClass extends Type {
	private SStringClass() {}

	public static final StringDict dict;
	public static final SStringClass instance = new SStringClass();
	public static final Obj constructor;
	
	static {
		if("SString".equals("NativeObj") || "SString".equals("UserObj"))
			dict = new StringDict();
		else
			dict = BaseStringClass.dict.copy();
		
		dict.put("join", new Method() {
			public Obj callMethod(Obj self, Obj[] args) {
				if(args.length != 1) {
					throw new ScriptError(ScriptError.TypeError, "Bad number of arguments");
				}
				return ((SString)self).join(args[0]);
			}
		});
		constructor = new Obj() {
			public Obj call(Obj[] args)  {
				if(args.length != 1) {
					throw new ScriptError(ScriptError.TypeError, "Bad number of arguments");
				}
				return SString.construct(args[0]);
			}
		};
		
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
