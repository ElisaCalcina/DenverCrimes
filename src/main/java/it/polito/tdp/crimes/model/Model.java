package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

//NON ABBIAMO USATO UNA IDMAP PERCHE' I VERTICI DEL GRAFO SONO DELLE STRINGHE --> LE STRINGHE HANNO GIA' UN LORO HASHCODE E EQUALS E 
//QUINDI NON C'E' IL RISCHIO DI CREARE DUE OGGETTI PER NOI UGUALI MA CHE POI SONO DIVERSI

//IDMAP EVITA DI CREARE PIU' VOLTE LE STESSE STRINGHE E AIUTA LE PERFORMANCE, MA NON E' UN PROBLEMA NON METTERLA IN QUESTO CASO

public class Model {
	private EventsDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	private List<String> best;
	
	public Model(){
		dao= new EventsDao();
	}
	
	
	public List<String> getCategoria(){
		return this.dao.getCategoria();
	}
	
	public List<Integer> getMesi(){
		return this.dao.getMesi();
	}
	
	public void creaGrafo(String categoria, Integer mese) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Adiacenza> adiacenze= this.dao.getAdiacenze(categoria, mese);
		
		for(Adiacenza a: adiacenze) {
			//inserisco i vertici dell'adiacenza considerata se questi non sono già presenti nel grafo
			if(!this.grafo.containsVertex(a.getObj1()))
				this.grafo.addVertex(a.getObj1());
			if(!this.grafo.containsVertex(a.getObj2()))
				this.grafo.addVertex(a.getObj2());
			
			//grafo NON orientato--> attenzione a non controllare le coppie due volte
			//se un arco tra due vertici c'è già non lo ricreo
			
			//se è null non c'è ancora allora lo inserisco
			if(this.grafo.getEdge(a.getObj1(), a.getObj2())==null) {
				Graphs.addEdgeWithVertices(grafo, a.getObj1(), a.getObj2(), a.getPeso());
			}
		}
		
		System.out.println(String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
		
	}
	//metodo che ritorni una lista di archi con peso maggiore del peso medio del grafo per il punto d
	public List<Arco> getArchi(){
		//deve calcolare il peso medio del grafo
		double pesoMedio=0.0;
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			pesoMedio+=this.grafo.getEdgeWeight(e);
		}
		pesoMedio= pesoMedio/this.grafo.edgeSet().size();
		
		//lista di archi con peso maggiore del peso medio
		List<Arco> archi= new ArrayList<Arco>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>pesoMedio) {
				archi.add(new Arco(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
			}
		}
		Collections.sort(archi);
		return archi;
		
	}
	
	//i vertici sono stringhe; metodo che imposta la ricorsione
	public List<String> trovaPercorso(String sorgente, String destinazione){
		List<String> parziale = new ArrayList<>();
		this.best= new ArrayList<>();
		
		//aggiungo nodo di partenza in parziale
		parziale.add(sorgente);
		trovaRicorsivo(destinazione,parziale, 0);
		
		return best;
	}

	//metodo ricorsivo
	private void trovaRicorsivo(String destinazione, List<String> parziale, int i) {
		// caso terminale? --> quando l'ultimo vertice inserito in parziale è uguale alla destinazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			//controllo la "bontà" del percorso trovato
			if(parziale.size()>this.best.size()) {
				this.best= new ArrayList<>(parziale);
			}
			return;
		}
		//scorro i vicini dell'ultimo vertice inserito in parziale
		for(String vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			//cammini aciclici--> non devo visitare lo stesso nodo più volte => controllo che il vertice non sia già in parziale
			if(!parziale.contains(vicino)) {
				//provo ad aggiungere
				parziale.add(vicino);
				//continuo la ricorsione
				this.trovaRicorsivo(destinazione, parziale, i+1);
				//faccio backtracking
				parziale.remove(parziale.size()-1);
			}
		}
	}
}
