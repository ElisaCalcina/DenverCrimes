package it.polito.tdp.crimes.model;

//creo questa classe perchè sarà più comodo stampare il punto d) (anche se è uguale ad adiacenza, quindi si può
//usare quella; la creiamo per pulizia
public class Arco implements Comparable<Arco> {
		private String obj1;
		private String obj2;
		private Double peso;
		
		public Arco(String obj1, String obj2, Double peso) {
			this.obj1 = obj1;
			this.obj2 = obj2;
			this.peso = peso;
		}

		public String getObj1() {
			return obj1;
		}

		public void setObj1(String obj1) {
			this.obj1 = obj1;
		}

		public String getObj2() {
			return obj2;
		}

		public void setObj2(String obj2) {
			this.obj2 = obj2;
		}

		public Double getPeso() {
			return peso;
		}

		public void setPeso(Double peso) {
			this.peso = peso;
		}

		@Override
		public int compareTo(Arco o) {
			return this.getPeso().compareTo(o.getPeso());
		}

		@Override
		public String toString() {
			return "Arco [obj1=" + obj1 + ", obj2=" + obj2 + ", peso=" + peso + "]";
		}
		
		
	
}