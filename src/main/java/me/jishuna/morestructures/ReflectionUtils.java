package me.jishuna.morestructures;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

import sun.misc.Unsafe;

public class ReflectionUtils {
	public static void setStaticFieldUsingUnsafe(final Field field, final Object newValue) {
		try {
			field.setAccessible(true);
			int fieldModifiersMask = field.getModifiers();
			boolean isFinalModifierPresent = (fieldModifiersMask & Modifier.FINAL) == Modifier.FINAL;
			if (isFinalModifierPresent) {
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					@Override
					public Object run() {
						try {
							Unsafe unsafe = getUnsafe();
							long offset = unsafe.staticFieldOffset(field);
							Object base = unsafe.staticFieldBase(field);
							setFieldUsingUnsafe(base, field.getType(), offset, newValue, unsafe);
							return null;
						} catch (Throwable t) {
							throw new RuntimeException(t);
						}
					}
				});
			} else {
				field.set(null, newValue);
			}
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static void setFieldUsingUnsafe(Object base, Class<?> type, long offset, Object newValue, Unsafe unsafe) {
		if (type == Integer.TYPE) {
			unsafe.putInt(base, offset, ((Integer) newValue));
		} else if (type == Short.TYPE) {
			unsafe.putShort(base, offset, ((Short) newValue));
		} else if (type == Long.TYPE) {
			unsafe.putLong(base, offset, ((Long) newValue));
		} else if (type == Byte.TYPE) {
			unsafe.putByte(base, offset, ((Byte) newValue));
		} else if (type == Boolean.TYPE) {
			unsafe.putBoolean(base, offset, ((Boolean) newValue));
		} else if (type == Float.TYPE) {
			unsafe.putFloat(base, offset, ((Float) newValue));
		} else if (type == Double.TYPE) {
			unsafe.putDouble(base, offset, ((Double) newValue));
		} else if (type == Character.TYPE) {
			unsafe.putChar(base, offset, ((Character) newValue));
		} else {
			unsafe.putObject(base, offset, newValue);
		}
	}

	private static Unsafe getUnsafe()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field field1 = Unsafe.class.getDeclaredField("theUnsafe");
		field1.setAccessible(true);
		Unsafe unsafe = (Unsafe) field1.get(null);
		return unsafe;
	}

}
