package cc.gu.util.value;

public interface OnValueSetListener <V>{
	public void onValueSeted(Valuable<? extends V> valuable, V value);
}
