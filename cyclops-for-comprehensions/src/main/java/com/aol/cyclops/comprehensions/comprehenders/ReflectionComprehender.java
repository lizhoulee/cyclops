package com.aol.cyclops.comprehensions.comprehenders;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.aol.cyclops.lambda.api.Comprehender;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReflectionComprehender implements Comprehender {

	Optional<Class> type;
	
	
	@Override
	public Object filter(Object t, Predicate p) {
		Method m = Stream.of(t.getClass().getMethods())
				.filter(method -> "filter".equals(method.getName()))
				.filter(method -> method.getParameterCount()==1)
				.findFirst().get();
		Class z = m.getParameterTypes()[0];
		Object o = Proxy.newProxyInstance(ReflectionComprehender.class
				.getClassLoader(), new Class[]{z}, (proxy,
				method, args) -> {
			return p.test(args[0]);
		});

		try {
			return m.invoke(t, o);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Object map(Object t, Function fn) {
		Method m = Stream.of(t.getClass().getMethods())
				.filter(method -> "map".equals(method.getName()))
				.filter(method -> method.getParameterCount()==1).findFirst()
				.get();
		Class z = m.getParameterTypes()[0];
		Object o = Proxy.newProxyInstance(ReflectionComprehender.class
				.getClassLoader(), new Class[]{z}, (proxy,
				method, args) -> {
			return fn.apply(args[0]);
		});

		try {
			return m.invoke(t, o);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object flatMap(Object t, Function fn) {
		
		Method m = Stream.of(t.getClass().getMethods())
				.filter(method -> "flatMap".equals(method.getName()))
				.filter(method -> method.getParameterCount()==1)
				.findFirst().orElseGet( ()-> Stream.of(t.getClass().getMethods())
						.filter(method -> "bind".equals(method.getName()))
						.filter(method -> method.getParameterCount()==1)
						.findFirst().get());
		
		Class z = m.getParameterTypes()[0];
		
		Object o = Proxy.newProxyInstance(ReflectionComprehender.class
				.getClassLoader(), new Class[]{z}, (proxy,
				method, args) -> {
			return fn.apply(args[0]);
		});
		

		try {
			return m.invoke(t, o);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean instanceOfT(Object apply) {
		return type.map(t -> apply.getClass().isAssignableFrom(t)).orElse(true);
	}

	@Override
	public Object of(Object o) {
		try {
			return type.get().getMethod("of",o.getClass()).invoke(null,o);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object of() {
		try {
			return type.get().getMethod("of").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	

}
