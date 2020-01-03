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
	private List<TypeReader> typeReaders = new ArrayList<>();
	private List<TypeWriter> typeWriters = new ArrayList<>();
	private Map<String, TypeReader> explicitReaderMap = new HashMap<>();
	private Map<Class<?>, TypeWriter> explicitWriterMap = Collections.emptyMap();
	private boolean updateExplicitWriterMap = false;

	@Override
	public void register(TypeHandler... typeHandlers) {
		Objects.requireNonNull(typeHandlers, "typeHandlers should not null");

		register((TypeReader[]) typeHandlers);
		register((TypeWriter[]) typeHandlers);
	}

	@Override
	public void register(TypeReader... typeReaders) {
		this.typeReaders.addAll(Arrays.asList(typeReaders));
		for (TypeReader typeReader : typeReaders) {
			if (typeReader instanceof ExplicitTypeReader) {
				for (String typeName : ((ExplicitTypeReader) typeReader).typeNames()) {
					explicitReaderMap.put(typeName, typeReader);
				}
			}
		}
	}

	@Override
	public void register(TypeWriter... multiTypeWriters) {
		this.typeWriters.addAll(Arrays.asList(multiTypeWriters));
		updateExplicitWriterMap = true;
	}

	@Override
	public Collection<TypeReader> typeReaders() {
		return typeReaders;
	}

	@Override
	public Collection<TypeWriter> typeWriters() {
		return typeWriters;
	}

	@Override
	public Map<String, TypeReader> explicitTypeReaderMap() {
		return explicitReaderMap;
	}

	@Override
	public Map<Class<?>, TypeWriter> explicitTypeWriterMap() {
		if (updateExplicitWriterMap) {
			// create new writer map
			List<ExplicitTypeWriter> ordered = new LinkedList<>();

			for (TypeWriter typeWriter : typeWriters) {
				if (!(typeWriter instanceof ExplicitTypeWriter)) {
					continue;
				}

				final ExplicitTypeWriter currentTypeWriter = (ExplicitTypeWriter) typeWriter;
				final ListIterator<ExplicitTypeWriter> it = ordered.listIterator();

				boolean added = false;
				while (it.hasNext()) {
					ExplicitTypeWriter possibleParent = it.next();
					if (possibleParent.typeClass().isAssignableFrom(currentTypeWriter.typeClass())) {
						it.previous();
						it.add(currentTypeWriter);
						added = true;
						break;
					}
				}
				if (!added) {
					ordered.add(currentTypeWriter);
				}
			}

			Map<Class<?>, TypeWriter> newMap = new LinkedHashMap<>(ordered.size());
			for (ExplicitTypeWriter typeWriter : ordered) {
				newMap.put(typeWriter.typeClass(), typeWriter);
			}
			explicitWriterMap = newMap;
		}

		return explicitWriterMap;
	}
}
