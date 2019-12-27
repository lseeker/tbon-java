package kr.inode.tbon.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

class TypeHandlerRegistryImpl implements TypeHandlerRegistry {
	private Map<String, TypeHandler> mapForReader = new HashMap<>();
	private Map<Class<?>, TypeHandler> mapForWriter = Collections.emptyMap();
	private List<MultiTypeReader> multiTypeReaders = new ArrayList<>();
	private List<MultiTypeWriter> multiTypeWriters = new ArrayList<>();
	private boolean needUpdateMapForWriter = false;

	@Override
	public void register(TypeHandler... typeHandlers) {
		Objects.requireNonNull(typeHandlers, "typeHandlers should not null");

		for (TypeHandler typeHandler : typeHandlers) {
			mapForReader.put(typeHandler.typeName(), typeHandler);
			needUpdateMapForWriter = true;
		}
	}

	@Override
	public void register(MultiTypeReader... multiTypeReaders) {
		this.multiTypeReaders.addAll(Arrays.asList(multiTypeReaders));
	}

	@Override
	public void register(MultiTypeWriter... multiTypeWriters) {
		this.multiTypeWriters.addAll(Arrays.asList(multiTypeWriters));
	}

	@Override
	public Map<String, TypeHandler> handlerMapForReader() {
		return mapForReader;
	}

	@Override
	public Map<Class<?>, TypeHandler> handlerMapForWriter() {
		if (needUpdateMapForWriter) {
			// create new writer map
			List<TypeHandler> ordered = new LinkedList<>();

			for (TypeHandler typeHandler : mapForReader.values()) {
				ListIterator<TypeHandler> it = ordered.listIterator();

				boolean added = false;
				while (it.hasNext()) {
					TypeHandler possibleParent = it.next();
					if (possibleParent.typeClass().isAssignableFrom(typeHandler.typeClass())) {
						it.previous();
						it.add(typeHandler);
						added = true;
						break;
					}
				}
				if (!added) {
					ordered.add(typeHandler);
				}
			}

			Map<Class<?>, TypeHandler> newMap = new LinkedHashMap<>(mapForReader.size());
			for (TypeHandler typeHandler : ordered) {
				newMap.put(typeHandler.typeClass(), typeHandler);
			}
			mapForWriter = newMap;
		}
		return mapForWriter;
	}

	@Override
	public Collection<MultiTypeReader> multiTypeReaders() {
		return multiTypeReaders;
	}

	@Override
	public Collection<MultiTypeWriter> multiTypeWriters() {
		return multiTypeWriters;
	}

}
