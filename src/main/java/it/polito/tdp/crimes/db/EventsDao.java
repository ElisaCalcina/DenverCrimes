package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getCategoria(){
		String sql="SELECT DISTINCT offense_category_id FROM events ";
		List<String> categorie = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				categorie.add(res.getString("offense_category_id"));
			}
			
			conn.close();
			return categorie;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<Integer> getMesi(){
		String sql="SELECT DISTINCT MONTH(REPORTED_DATE) AS mesi FROM events ";
		List<Integer> mesi= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				mesi.add(res.getInt("mesi"));
			}
			
			conn.close();
			Collections.sort(mesi);
			return mesi;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<Adiacenza> getAdiacenze(String categoria, Integer mese){
		//reato a e b sono collegati da arco che ha peso pari al numero di quartieri distinti in cui si sono verificati sia a che b
		//tutto in un determinato mese e fanno parte solo della categoria specificata dall'utente all'inizio
		
		//coppie di vertici--> join tabella con se stessa
		//per ogni coppia di reati distinti all'interno di una data categoria e di un dato mese, vado a contare in quanti quartieri si sono verificati entrambi i reati
		String sql="SELECT e1.offense_type_id as v1, e2.offense_type_id as v2, COUNT(DISTINCT(e1.neighborhood_id)) AS peso " + 
				"FROM EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_category_id=?" + 
				"		AND e2.offense_category_id=? " + 
				"		AND MONTH(e1.reported_date) = ? " + 
				"		AND MONTH(e2.reported_date)=? " + 
				"		AND e1.offense_type_id!=e2.offense_type_id " + 
				"		AND e1.neighborhood_id=e2.neighborhood_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id ";
	
		List<Adiacenza> adiacenze= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, mese);
			st.setInt(4, mese);

			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				adiacenze.add(new Adiacenza(res.getString("v1"), res.getString("v2"), res.getDouble("peso")));
			}
			
			conn.close();
			return adiacenze;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
 
}
