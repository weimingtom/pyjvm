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

import java.util.Arrays;

public final class StringDict extends Obj {
	public final class Entry {
		public Entry(int key, Obj val) {
			this.key = key;
			this.val = val;
		}
		
		public Entry copy() {
			Entry copied = new Entry(key, val);
			if(next != null)
				copied.next = next.copy();
			return copied;
		}
		
		public int key;
		public Obj val;
		private Entry next;
	}
	
	private Entry[] entries;
	private int resizeAt;
	private int length = 0;
	
	private static final float scaleFactor = 1.35f;
	
	public StringDict(int initalCapacity) {
		entries = new Entry[(int)(initalCapacity * scaleFactor)];
		resizeAt = initalCapacity;
	}
	
	public StringDict(StringDict original) {
		Entry[] entries = new Entry[original.entries.length];
		for(int i=0; i<entries.length; ++i) {
			Entry old = original.entries[i];
			entries[i] = (old == null) ? null : old.copy();
		}
		this.entries = entries;
	}
	
	public final void put(int key, Obj val) {
		int hash = key;
		int index = Math.abs(hash % entries.length);
		Entry e = entries[index];
		while(e != null) {
			if(e.key == key) {
				e.val = val;
				return;
			}
			if(e.next == null)
				break;
			else
				e = e.next;
		}
		Entry entry = new Entry(key, val);
		Object next__ = e==null? "<null>" : e.next + "";
		if(e != null)
			e.next = entry;
		else
			entries[index] = entry;
		length++;
		if(length >= resizeAt) this.resize();
	}
	
	public final void put(SString key, Obj val) {
		put(key.intern(), val);
	}
	
	public final void put(String string, Obj val) {
		put(SString.fromJavaString(string), val);
	}
	
	private void resize() {
		Entry[] newEntries = new Entry[this.entries.length * 2];
		Entry[] oldEntries = this.entries;
		this.entries = newEntries;
		this.resizeAt = (int) (scaleFactor * newEntries.length);
		
		for(int i=0; i<oldEntries.length; i++) {
			Entry e = oldEntries[i];
			while(e != null) {
				put(e.key, e.val);
				e = e.next;
			}
		}
	}
	
	public final Obj getOrNull(int key) {
		int hash = key;
		int index = Math.abs(hash % entries.length);
		Entry e = entries[index];
		while(e != null) {
			if(e.key == key)
				return e.val;
			e = e.next;
		}
		return null;
	}
	
	public final Obj get(int key) {
		int hash = key;
		int index = Math.abs(hash % entries.length);
		Entry e = entries[index];
		while(e != null) {
			if(e.key == key)
				return e.val;
			e = e.next;
		}
		SString keyString = SString.uninternQuiet(key);
		throw new ScriptError(ScriptError.AttributeError, "not found: " + keyString);
	}
	 
	public final void delete(int key) {
		int hash = key;
		int index = Math.abs(hash % entries.length);
		Entry prev = null;
		Entry e = entries[index];
		while(e != null) {
			if(e.key == key) {
				if(prev == null)
					entries[index] = e.next;
				else
					prev.next = e.next;
				return;
			}
			prev = e;
			e = e.next;
		}
		SString keyString = SString.uninternQuiet(key);
		throw new ScriptError(ScriptError.AttributeError, "not found: " + keyString);
	}
	
	public final Obj get(String s) {
		return get(SString.intern(s));
	}
	
	public final StringDict copy() {
		return new StringDict(this);
	}
	
	public StringDict() {
		this(8);
	}

	public SBool isEqual() {
		// TODO: isEqual
		throw new RuntimeException("Not implemented.");
	}
	public int hashCode() {
		// TODO: hashCode
		throw new RuntimeException("Not implemented.");
	}
	
	
	public String toString() {
		DictEntryIterator iterator = new DictEntryIterator();
		Entry entry;
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		while((entry=iterator.next()) != null) {
			builder.append(SString.uninternQuiet(entry.key).repr());
			builder.append(": ");
			builder.append(entry.val.repr().toString());
			builder.append(", ");
		}
		builder.append("}");
		return builder.toString();
	}
	
	public final class DictKeyIterator {
		private int entryIndex = 0;
		private Entry entry = null;
		
		public int next() {
			if(entry == null) {
				entry = entries[entryIndex];
				entryIndex ++;
				if(entryIndex == entries.length)
					return -1; // string keys are always >=0
			}
			int key = entry.key;
			entry = entry.next;
			return key;
		}
	}
	public final class DictEntryIterator {
		private int entryIndex = 0;
		private Entry entry = null;
		
		public Entry next() {
			while(entry == null) {
				if(entryIndex == entries.length)
					return null;
				entry = entries[entryIndex];
				entryIndex ++;
			}
			Entry thisEntry = entry;
			entry = entry.next;
			return thisEntry;
		}
	}
	public void update(StringDict dict) {
		DictEntryIterator iter = dict.entryIterator();
		Entry entry;
		while((entry=iter.next()) != null)
			put(entry.key, entry.val);
	}

	public DictEntryIterator entryIterator() {
		return new DictEntryIterator();
	}
}
