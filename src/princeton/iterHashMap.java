package princeton;

import java.util.HashMap;
import java.util.List;
public class iterHashMap<K, V,I> extends HashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public iterHashMap() {
		super();
	}
	public boolean iterContainsValue(I i) {
		for(Object o: this.keySet()) {
			List<I> list = (List<I>)(this.get(o));
			if (!(list.contains(i))) continue;
			return true;
		}
		return false;
			
			
		}
	

}
