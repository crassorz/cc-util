package cc.gu.util.factory;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SupplyChain {

	 final private SupplyChain[] baseSupplyChains;
	 final private SupplyChain[] supplyChains;
	 public SupplyChain(SupplyChain...baseSupplyChains) {
		 this.baseSupplyChains = baseSupplyChains;
		 supplyChains = new SupplyChain[baseSupplyChains.length + 1];
		 supplyChains[0] = this;
		 copyTo(baseSupplyChains, 0, baseSupplyChains.length, supplyChains, 1);
	 }
	 
	 
	 private static <TO, FROM extends TO>void copyTo(FROM[] from, int start, int length, TO[] to, int toStart) {
		 for (int i = 0; i < length; i++) {
			 to[toStart + i] = from[start + i];
		}
	 }
	
	private static SupplyChain instance;
	synchronized public static SupplyChain getInstance() {
		if (instance == null) {
			instance = new SupplyChain();
		}
		return instance;
	}
	final private List<Factory<?,?>> builders = new ArrayList<Factory<?,?>>();
	final private Comparator<Factory<?,?>> comparator = new Comparator<Factory<?,?>>() {
        public int compare(Factory<?,?> o1, Factory<?,?> o2) {
        	int compare = o2.priority - o1.priority;
        	if (compare == 0) {
        		compare = o2.id - o1.id > 0 ? 1 : -1;
        	}
            return compare;
        }
    };
	
	private static volatile long factoryId = Long.MIN_VALUE;
    private volatile boolean needUpdate = false;

	synchronized private SupplyChain[] getSupplyChains() {
		return supplyChains;
	}
	synchronized private List<Factory<?,?>> getFactorys() {
		update();
		return builders;
	}
	
	private boolean needUpdate() {
		if (needUpdate) return true;
		return false;
	}
	
    synchronized private void update() {
		if (needUpdate()) {
			needUpdate = false;
			synchronized (SupplyChain.class) {
				Collections.sort(builders, comparator);
			}
		}
    }

    @SuppressWarnings("unchecked")
	private <PRODUCT> Factory<PRODUCT, ?> getFactory(Provider<PRODUCT> provider, Object raw) {
		for (Factory<?, ?> factory : getFactorys()) {
			if (valid(provider, factory, raw)) {
				return (Factory<PRODUCT, ?>) factory;
			}
		}
		Factory<PRODUCT, ?> factory;
		for (SupplyChain supplyChain : baseSupplyChains) {
			factory = supplyChain.getFactory(provider, raw);
			if (factory != null) return factory;
		}
		return null;
    }
    
    private static <PRODUCT, RAW> boolean valid(Provider<PRODUCT> provider, Factory<?, ?> factory, RAW raw) {
			if (raw != factory.rawClass.cast(raw)) return false;
			if (!factory.productClass.isAssignableFrom(provider.productClass)) return false;
			if (!factory.validRaw(raw)) return false;
			if (!provider.validFactory(factory)) return false;
			return true;
    }
    
    abstract public static class Provider<PRODUCT> {

    	final private Class<PRODUCT> productClass;
    	final private SupplyChain supplyChain;
    	

		public Provider() {
			this(getInstance());
		}
		@SuppressWarnings("unchecked")
		public Provider(SupplyChain supplyChain) {
			if (getClass().getSuperclass() != Provider.class) throw new ClassFormatError(getClass()  + " should extends Provider only");
			this.supplyChain = supplyChain;
			ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
			productClass = (Class<PRODUCT>) pt.getActualTypeArguments()[0];
		}
		final public Factory<PRODUCT, ?> getFactory(Object raw) {
			return supplyChain.getFactory(this, raw);
		}

		
		@SuppressWarnings("unchecked")
		private boolean validFactory(Factory<?, ?> factory) {
			return isValidFactory((Factory<? extends PRODUCT, ?>)factory);
		}

		/**
		 * check the factory is valid for your provider
		 */
		protected boolean isValidFactory(Factory<? extends PRODUCT, ?> factory) {
			return factory != null;
		}
		
		final public PRODUCT getProduct(PRODUCT product, Object raw) {
			Factory<? extends PRODUCT, ?> factory = getFactory(raw);
			if (factory != null) {
				if (product == null) {
					product = factory.getProduct();
				}
				return factory.updateProduct(product, raw);
			} else {
				return null;
			}
		}
    }
	
	abstract public static class Factory<PRODUCT, RAW> {
		
		
		@Override
		public int hashCode() {
			return priority;
		}

		final private Class<PRODUCT> productClass;
    	final private Class<RAW> rawClass;
		final private int priority;
		final private long id;
		/**
		 * priority = 0
		 */
		public Factory() {
			this(0);
		}
		/**
		 * more high priority will more fast check canBuild or not
		 */
		public Factory(int priority) {
			this(getInstance(), priority);
		}
		/**
		 * priority = 0
		 */
		public Factory(SupplyChain supplyChain) {
			this(supplyChain, 0);
		}
		/**
		 * more high priority will more fast check canBuild or not
		 */
		@SuppressWarnings("unchecked")
		public Factory(SupplyChain supplyChain, int priority) {
			if (getClass().getSuperclass() != Factory.class) throw new ClassFormatError(getClass()  + " should extends Factory only");
			ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
			productClass = (Class<PRODUCT>) pt.getActualTypeArguments()[0];
			rawClass = (Class<RAW>) pt.getActualTypeArguments()[1];
			this.priority = priority;
			synchronized (supplyChain) {
				id = ++factoryId;
				supplyChain.builders.add(this);
				supplyChain.needUpdate = true;
			}
		}
		
		@SuppressWarnings("unchecked")
		private boolean validRaw(Object raw) {
			return isValidRaw((RAW)raw);
		}

		/**
		 * check the raw is valid on your product
		 */
		protected boolean isValidRaw(RAW raw) {
			return raw != null;
		}
		
		abstract protected PRODUCT getProduct();
		@SuppressWarnings("unchecked")
		private PRODUCT updateProduct(Object product, Object raw) {
			return getProduct((PRODUCT)product, (RAW)raw);
		}
		protected PRODUCT getProduct(PRODUCT product, RAW raw) {
			return product;
		}
	}
}
