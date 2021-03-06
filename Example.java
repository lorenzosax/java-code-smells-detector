package gestori;

import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import classi.Automobile;
import classi.Guidatore;
import classi.Itinerario;
import classi.Pagamento;
import classi.PagamentoCarta;
import classi.PagamentoContanti;
import classi.Prenotazione;
import classi.PrenotazioneTratta;
import classi.Tratta;
import classi.Utente;
import classi.Valutazione;
import classi.ValutazioneAutomobile;
import classi.ValutazioneGuidatore;

public class ControllerSyncar {

	private static ControllerSyncar instance;
	private GestoreDB gestoreDB;
	private GestorePrenotazioni gestorePrenotazioni;
	private GestoreItinerari gestoreItinerari;
	private GestoreValutazioni gestoreValutazioni;

	public ControllerSyncar() throws SQLException {
		this.gestoreDB = GestoreDB.getInstance();
		gestoreDB.connessioneDB();
		this.gestorePrenotazioni = GestorePrenotazioni.getInstance(gestoreDB.leggiUtenti(),
				gestoreDB.leggiPrenotazioni(), gestoreDB.leggiItinerari(), gestoreDB.leggiUtentiItinerari(),
				gestoreDB.leggiPagamenti(), gestoreDB.leggiPrenotazioniTratte(), gestoreDB.leggiTratte());
		this.gestoreItinerari = GestoreItinerari.getInstance(gestoreDB.leggiItinerari(), gestoreDB.leggiTratte(),
				gestoreDB.leggiGuidatori(), gestoreDB.leggiAutomobili());
		this.gestoreValutazioni = GestoreValutazioni.getInstance(gestoreDB.leggiValutazioni(),
				gestoreDB.leggiGuidatori(), gestoreDB.leggiAutomobili(), gestoreDB.leggiUtenti());

	}

	public static ControllerSyncar getInstance() throws SQLException {
		if (instance == null)
			instance = new ControllerSyncar();
		return instance;
	}

	public GestoreDB getGestoreDB() {
		return gestoreDB;
	}

	public GestorePrenotazioni getGestorePrenotazioni() {
		return gestorePrenotazioni;
	}

	public GestoreItinerari getGestoreItinerari() {
		return gestoreItinerari;
	}

	public GestoreValutazioni getGestoreValutazioni() {
		return gestoreValutazioni;
	}

	public void setGestoreDB(GestoreDB gestoreDB) {
		this.gestoreDB = gestoreDB;
	}

	public void setGestorePrenotazioni(GestorePrenotazioni gestorePrenotazioni) {
		this.gestorePrenotazioni = gestorePrenotazioni;
	}

	public void setGestoreItinerari(GestoreItinerari gestoreItinerari) {
		this.gestoreItinerari = gestoreItinerari;
	}

	public void setGestoreValutazioni(GestoreValutazioni gestoreValutazioni) {
		this.gestoreValutazioni = gestoreValutazioni;
	}

	public boolean verificaTratte(int idItineario, String partenza, String destinazione) {
		if (partenza.equals(destinazione))
			return false;

		Itinerario itinerario = gestoreItinerari.filtraItinerario(idItineario);

		int p = 0;
		int di = 0;

		Tratta trattaP = gestoreItinerari.filtraTrattaPartenza(itinerario.getTratte(), partenza);
		Tratta trattaD = gestoreItinerari.filtraTrattaDestinazione(itinerario.getTratte(), destinazione);

		for (Tratta t : itinerario.getTratte()) {
			if (t.getId() == trattaP.getId())
				p = itinerario.getTratte().indexOf(t);
			if (t.getId() == trattaD.getId())
				di = itinerario.getTratte().indexOf(t);
		}

		if (itinerario.getLuogo_partenza().equals(partenza) && itinerario.getLuogo_destinazione().equals(destinazione))
			return false;
		if (p <= di)
			return true;
		if (p > di)
			return false;

		return false;
	}

	public boolean verificaCredenziali(String codice_carta, Date data_scadenza) {
		for (PagamentoCarta p : gestorePrenotazioni.getPagamentiCarta()) {
			if (codice_carta.equals(p.getCodice_carta()) && data_scadenza.equals(p.getData_scadenza()))
				return true;
		}
		return false;
	}

	public boolean verificaLogin(String username, String password) {
		for (Utente u : gestorePrenotazioni.getUtenti()) {
			if (u.getNomeUtente().equals(username) && password.equals(u.getPassword()))
				return true;
		}

		return false;
	}


	public ArrayList/*<String>*/ getListaItinerari(String username) {
		//ArrayList<String> lista = new ArrayList<String>();
		for (Itinerario i : gestoreItinerari.getItinerariDisponibili(username)) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String date = df.format(i.getData_partenza());
			String element = i.getId() + " " + i.getLuogo_partenza() + " " + i.getLuogo_destinazione() + " " + date + " EURO " + i.getPrezzo();
			lista.add(element);
		}

		return lista;
	}

	public String getListaPrenotazioniTratte(String username, Prenotazione prenotazione) {
		String lista = prenotazione.getTratte().get(0).getTratta().getLuogo_partenza();
		for (int i = 0; i < prenotazione.getTratte().size(); i++) {
			lista = lista.concat(" - " + prenotazione.getTratte().get(i).getTratta().getLuogo_destinazione());
		}

		return lista;
	}

	public ArrayList/*<String>*/ getListaPrenotazioni(String username) {
		//ArrayList<String> lista = new ArrayList<String>();
		for (Prenotazione p : gestorePrenotazioni.getPrenotazioniUtente(username)) {
			Itinerario i = gestorePrenotazioni.filtraItinerario(p.getIdItinerario());
			if (i != null) {
				Date date1 = new Date(i.getData_partenza().getYear(), i.getData_partenza().getMonth(),
						i.getData_partenza().getDate(), i.getOrario_destinazione().getHours(),
						i.getOrario_destinazione().getMinutes());
				if ((getDateDiff(new Date(), date1) > 60) && !(p.isAnnullata())) {
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					String date = df.format(i.getData_partenza());
					String element = p.getId() + " - Itinerario " + i.getId() + " - " + i.getLuogo_partenza() + " - "
							+ i.getLuogo_destinazione() + " - " + date + " - € " + i.getPrezzo();
					lista.add(element);
				}
			}
		}

		return lista;
	}

	public long getDateDiff(Date date1, Date date2) {
		long diffInMinutes = date2.getTime() - date1.getTime();
		return (long) (diffInMinutes * 1);
	}

	public ArrayList/*<String>*/ getListaItinerariDaValutare(String username) {
		//ArrayList<String> lista = new ArrayList<String>();
		for (Prenotazione p : gestorePrenotazioni.getPrenotazioniUtente(username)) {
			Itinerario i = gestorePrenotazioni.filtraItinerario(p.getIdItinerario());
			if (i != null) {
				Date date1 = new Date(i.getData_partenza().getYear(), i.getData_partenza().getMonth(),
						i.getData_partenza().getDate(), i.getOrario_destinazione().getHours(),
						i.getOrario_destinazione().getMinutes());
				if (!p.isAnnullata() && date1.before(new Date())
						&& (!valutazioneAutomobileEffettuata(username) || !valutazioneGuidatoreffettuata(username))) {
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					String date = df.format(i.getData_partenza());
					String element = i.getId() + " - " + i.getLuogo_partenza() + " - " + i.getLuogo_destinazione()
							+ " - " + date + " - € " + i.getPrezzo();
					lista.add(element);
				}
			}
		}

		return lista;
	}

	public boolean valutazioneAutomobileEffettuata(String username) {
		for (Valutazione v : gestoreValutazioni.getValutazioni()) {
			if (v instanceof ValutazioneAutomobile) {
				if (v.getCodiceFiscaleUtente()
						.equals(gestorePrenotazioni.filtraUtenteUsername(username).getCodiceFiscale()))
					return true;
			}
		}

		return false;
	}

	public boolean valutazioneGuidatoreffettuata(String username) {
		for (Valutazione v : gestoreValutazioni.getValutazioni()) {
			if (v instanceof ValutazioneGuidatore) {
				if (v.getCodiceFiscaleUtente()
						.equals(gestorePrenotazioni.filtraUtenteUsername(username).getCodiceFiscale()))
					return true;
			}
		}

		return false;
	}


	public void richiediPrenotazione(int id, Date data, int posti, String utente, int itinerario, int pagamento,
									 boolean partecipazione, String partenza, String destinazione) throws SQLException {
		Prenotazione p = new Prenotazione(id, data, posti, utente, itinerario, pagamento);
		Utente u = gestorePrenotazioni.filtraUtente(utente);
		u.aggiungiPrenotazione(p);
		p.setUtente(u);
		gestorePrenotazioni.aggiungiPrenotazione(p);
		gestoreDB.inserimentoPrenotazione(p);
		int postiAttuali = gestoreItinerari.filtraItinerario(itinerario).getPosti();
		int postiIt = postiAttuali - posti;
		Itinerario i = gestoreItinerari.filtraItinerario(itinerario);
		p.setItinerario(i);

		if (!(i.getPosti() == 0)) {
			gestoreDB.aggiornamentoPostiItinerario(itinerario, postiIt);
			i.setPosti(postiIt);
		}

		if (!partecipazione) {
			for (Tratta t : i.getTratte()) {
				int postiTr = t.getPosti() - posti;
				t.setPosti(postiTr);
				gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
			}
		} else {
			for (Tratta t : gestoreItinerari.filtraTrattePrenotate(itinerario, partenza, destinazione)) {
				if (!(t.getPosti() == 0)) {
					int p1 = t.getPosti() - posti;
					t.setPosti(p1);
					gestoreDB.aggiornamentoPosti(t.getId(), p1);
				}
				gestoreDB.inserimentoPrenotazioneTratta(new PrenotazioneTratta(id, t.getId()));
			}
		}

	}

	public void richiediPagamento(int id, float costo, boolean tipo, String codiceCarta, Date scadenza, String utente)
			throws SQLException {
		Pagamento p = null;
		if (!tipo) {
			p = new PagamentoContanti(id, costo, utente);
		} else {
			p = new PagamentoCarta(id, costo, utente, codiceCarta, scadenza);
		}
		gestorePrenotazioni.getPagamenti().add(p);
		gestoreDB.inserimentoPagamento(p);
	}

	public float elaboraPrezzo(int id, String partenza, String destinazione) {
		float prezzo = 0;
		Itinerario itinerario = gestoreItinerari.filtraItinerario(id);

		int p = 0;
		int di = 0;

		Tratta trattaP = gestoreItinerari.filtraTrattaPartenza(itinerario.getTratte(), partenza);
		Tratta trattaD = gestoreItinerari.filtraTrattaDestinazione(itinerario.getTratte(), destinazione);

		for (Tratta t : itinerario.getTratte()) {
			if (t.getId() == trattaP.getId())
				p = itinerario.getTratte().indexOf(t);
			if (t.getId() == trattaD.getId())
				di = itinerario.getTratte().indexOf(t);
		}

		while (p <= di) {
			prezzo = prezzo + itinerario.getTratte().get(p).getPrezzo();
			p++;
		}

		return prezzo;
	}

	public int elaboraPosti(int id, String partenza, String destinazione) {
		int posti = 0;

		Itinerario itinerario = gestoreItinerari.filtraItinerario(id);

		int p = 0;
		int di = 0;

		Tratta trattaP = gestoreItinerari.filtraTrattaPartenza(itinerario.getTratte(), partenza);
		Tratta trattaD = gestoreItinerari.filtraTrattaDestinazione(itinerario.getTratte(), destinazione);

		for (Tratta t : itinerario.getTratte()) {
			if (t.getId() == trattaP.getId())
				p = itinerario.getTratte().indexOf(t);
			if (t.getId() == trattaD.getId())
				di = itinerario.getTratte().indexOf(t);
		}

		posti = itinerario.getTratte().get(p).getPosti();

		for (int i = p; i <= di; i++) {
			if (itinerario.getTratte().get(i).getPosti() < posti)
				posti = itinerario.getTratte().get(i).getPosti();
		}

		return posti;
	}

	public boolean verificaPosti(int id, int posti) {
		if (gestoreItinerari.filtraItinerario(id).getPosti() >= posti) {
			return true;
		}
		return false;
	}

	public boolean verificaPosti(int id, int posti, String partenza, String destinazione) {
		int postiDisp = elaboraPosti(id, partenza, destinazione);
		if (postiDisp >= posti) {
			return true;
		}
		return false;
	}

	public boolean verificaOrario(Itinerario itinerario) {
		return false;
	}


	public void richiediCancellazionePrenotazione(Prenotazione prenotazione, String username) throws SQLException {
		prenotazione.setAnnullata(true);
		gestorePrenotazioni.filtraUtenteUsername(username).rimuoviPrenotazione(prenotazione);

		int postiIt = prenotazione.getItinerario().getPosti() + prenotazione.getPosti();
		prenotazione.getItinerario().setPosti(postiIt);
		gestoreDB.aggiornamentoPostiItinerario(prenotazione.getIdItinerario(), postiIt);

		if (!prenotazioneTratta(prenotazione)) {
			for (Tratta t : prenotazione.getItinerario().getTratte()) {
				int postiTr = t.getPosti() + prenotazione.getPosti();
				t.setPosti(postiTr);
				gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
			}
		} else {
			for (PrenotazioneTratta pt : prenotazione.getTratte()) {
				System.out.println(pt.getIdPrenotazione());
				Tratta t = gestorePrenotazioni.filtraTratta(pt.getIdTratta());
				int postiTr = t.getPosti() + prenotazione.getPosti();
				t.setPosti(postiTr);
				gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
			}
		}

		gestoreDB.aggiornamentoPrenotazione(prenotazione);
	}

	public boolean prenotazioneTratta(Prenotazione prenotazione) {
		if (prenotazione.getTratte().size() == 0)
			return false;

		return true;
	}

	public boolean verificaDatiItinerario(Time orario_partenza, Date data_partenza, String luogo_partenza,
										  Time orario_destinazione, String luogo_destinazione, boolean disponibile, float prezzo, String targa,
										  String modello, int posti) {
		return false;
	}

	public void richiediTratta(Time orario_partenza, String luogo_partenza, Time orario_destinazione,
							   String luogo_destinazione) {
		if (!prenotazioneTratta(prenotazione)) {
			for (Tratta t : prenotazione.getItinerario().getTratte()) {
				int postiTr = t.getPosti() + prenotazione.getPosti();
				t.setPosti(postiTr);
				gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
			}
		} else if (ok && !yes) {
			for (PrenotazioneTratta pt : prenotazione.getTratte()) {
				System.out.println(pt.getIdPrenotazione());
				Tratta t = gestorePrenotazioni.filtraTratta(pt.getIdTratta());
				int postiTr = t.getPosti() + prenotazione.getPosti();
				t.setPosti(postiTr);
				gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
			}
		} else {
			if (!prenotazioneTratta(prenotazione)) {
				for (Tratta t : prenotazione.getItinerario().getTratte()) {
					int postiTr = t.getPosti() + prenotazione.getPosti();
					t.setPosti(postiTr);
					gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
				}
			} else {
				for (PrenotazioneTratta pt : prenotazione.getTratte()) {
					System.out.println(pt.getIdPrenotazione());
					Tratta t = gestorePrenotazioni.filtraTratta(pt.getIdTratta());
					int postiTr = t.getPosti() + prenotazione.getPosti();
					t.setPosti(postiTr);
					gestoreDB.aggiornamentoPosti(t.getId(), postiTr);
				}
			}
		}


	}
	/*
      public void richiediCreazioneItinerario() {

      }

      public void richiediCancellazioneItinerario() {

      }
    */
	public void inviaDatiValutazioneGuidatore(int punteggio, String commento, String username, int idGuidatore)
			throws SQLException {
		Utente u = gestorePrenotazioni.filtraUtenteUsername(username);
		ValutazioneGuidatore vg = new ValutazioneGuidatore(punteggio, u.getCodiceFiscale(), commento, idGuidatore);
		gestoreValutazioni.aggiungiValutazioneGuidatore(vg);
		gestoreItinerari.filtraGuidatore(idGuidatore).aggiungiValutazioneGuidatore(vg);
		gestoreDB.inserimentoValutazioneGuidatore(vg, gestoreItinerari.filtraGuidatore(idGuidatore).getIdGuidatore());
	}


	public void inviaDatiValutazioneAutomobile(int punteggio, String username, boolean comodita, boolean igiene,
											   String targa) throws SQLException {
		Utente u = gestorePrenotazioni.filtraUtenteUsername(username);
		ValutazioneAutomobile va = new ValutazioneAutomobile(punteggio, u.getCodiceFiscale(), comodita, igiene, targa);
		gestoreValutazioni.aggiungiValutazioneAutomobile(va);
		gestoreItinerari.filtraAutomobile(targa).aggiungiValutazioneAutomobile(va);
		gestoreDB.inserimentoValutazioneAutomobile(va);
	}

	public float punteggioGuidatore(Guidatore guidatore) {
		float pt = 0;
		for (ValutazioneGuidatore vg : gestoreValutazioni.filtraValutazioniGuidatore(guidatore.getIdGuidatore())) {
			pt = pt + vg.getPunteggio();
		}
		return pt;
	}

	public boolean verificaUtente(String username) {
		String cf = gestorePrenotazioni.filtraUtenteUsername(username).getCodiceFiscale();
		Guidatore g = gestoreItinerari.filtraGuidatore(cf);
		if (g != null)
			return true;
		return false;
	}

	public void richiediItinerario(int id, Date dataP, Date orarioP, String luogoP, Date orarioD, String luogoD,
								   int posti, float prezzo, String username, String targa, String modello) throws SQLException {
		String cfGuidatore = gestorePrenotazioni.filtraUtenteUsername(username).getCodiceFiscale();
		Guidatore guidatore = gestoreItinerari.filtraGuidatore(cfGuidatore);
		Automobile a = new Automobile(targa, modello, posti, false, guidatore.getIdGuidatore());
		Itinerario i = new Itinerario(id, dataP, orarioP, luogoP, luogoD, posti, orarioD, prezzo, cfGuidatore, targa);
		i.setAutomobile(a);
		i.setCodiceFiscaleGuidatore(cfGuidatore);
		i.setGuidatore(guidatore);
		a.setGuidatore(guidatore);

		guidatore.aggiungiItinerario(i);

		gestoreItinerari.aggiungiItinerario(i);
		if (gestoreItinerari.filtraAutomobile(targa) == null) {
			gestoreItinerari.aggiungiAutomobile(a);
			gestoreDB.inserimentoAutomobile(a);
			guidatore.aggiungiAutomobile(a);
		}
		gestoreDB.inserimentoItinerario(i);
	}

	public Itinerario creaItinerarioPerTratte(int id, Date dataP, Date orarioP, String luogoP, Date orarioD,
											  String luogoD, int posti, float prezzo, String username, String targa, String modello) {
		String cfGuidatore = gestorePrenotazioni.filtraUtenteUsername(username).getCodiceFiscale();
		Guidatore guidatore = gestoreItinerari.filtraGuidatore(cfGuidatore);
		Automobile a = new Automobile(targa, modello, posti, false, guidatore.getIdGuidatore());
		Itinerario i = new Itinerario(id, dataP, orarioP, luogoP, luogoD, posti, orarioD, prezzo, cfGuidatore, targa);
		i.setAutomobile(a);
		i.setCodiceFiscaleGuidatore(cfGuidatore);
		i.setGuidatore(guidatore);
		a.setGuidatore(guidatore);

		guidatore.aggiungiItinerario(i);

		if (gestoreItinerari.filtraAutomobile(targa) == null) {
			guidatore.aggiungiAutomobile(a);
		}
		return i;
	}

	public void richiediItinerarioETratte(Itinerario itinerario/*, ArrayList<Tratta> tratte*/) throws SQLException {
		itinerario.setTratte(tratte);
		gestoreItinerari.aggiungiItinerario(itinerario);
		gestoreItinerari.aggiungiTratte(tratte);

		if (gestoreItinerari.filtraAutomobile(itinerario.getTargaAutomobile()) == null) {
			gestoreItinerari.aggiungiAutomobile(itinerario.getAutomobile());
			gestoreDB.inserimentoAutomobile(itinerario.getAutomobile());
		}

		gestoreDB.inserimentoItinerario(itinerario);
		gestoreDB.inserimentoTratte(tratte);
	}

}
