package management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import base.CommitterBadCodeSmell;
import base.BadCodeSmell;
import base.Change;
import base.Commit;
import base.Committer;
import base.DeadCode;
import base.DuplicatedCode;
import base.LargeClass;
import base.LongMethod;
import base.LongParameterList;
import base.Range;

public class Example {

	private HashMap<String, CommitterBadCodeSmell> associations;
	private HashMap<String, Committer> committers;
	private HashSet<BadCodeSmell> badCodeSmells;

	public Example(HashMap<String, Commit> commits, ArrayList<BadCodeSmell> badCodeSmells) {

		associations = new HashMap<String, CommitterBadCodeSmell>();
		committers = new HashMap<String, Committer>();
		this.badCodeSmells = new HashSet<BadCodeSmell>();

		for (Commit c : commits.values()) {
			for (Change ch : c.getChanges()) {
				for (Range r : ch.getRanges()) {
					for (BadCodeSmell b : badCodeSmells) {

						if (r.getChange().getFile().equals(b.getFile())) {

							if (r.getStartRow() <= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() >= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) <= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() <= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getStartRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() <= b.getEndRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));
							}
						}
					}
				}
			}
		}
		for (Commit c : commits.values()) {
			for (Change ch : c.getChanges()) {
				for (Range r : ch.getRanges()) {
					for (BadCodeSmell b : badCodeSmells) {

						if (r.getChange().getFile().equals(b.getFile())) {

							if (r.getStartRow() <= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() >= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) <= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() <= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getStartRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() <= b.getEndRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));
							}
						}
					}
				}
			}
		}
		for (Commit c : commits.values()) {
			for (Change ch : c.getChanges()) {
				for (Range r : ch.getRanges()) {
					for (BadCodeSmell b : badCodeSmells) {

						if (r.getChange().getFile().equals(b.getFile())) {

							if (r.getStartRow() <= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() >= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) <= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() <= b.getStartRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getStartRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));

							} else if (r.getStartRow() <= b.getEndRow()
									&& (r.getStartRow() + r.getInterval()) >= b.getEndRow()) {

								String badCodeSmellId = getTypeBadCodeSmell(b) + b.getFile() + b.getStartRow()
										+ b.getEndRow();

								if (committers.get(r.getChange().getCommit().getCommitter().getEmail()) == null) {
									committers.put(r.getChange().getCommit().getCommitter().getEmail(),
											r.getChange().getCommit().getCommitter());
								}

								committers.get(r.getChange().getCommit().getCommitter().getEmail()).addBadCodeSmells(b);
								b.addCommitter(committers.get(r.getChange().getCommit().getCommitter().getEmail()));

								this.badCodeSmells.add(b);

								associations.put(r.getChange().getCommitId() + badCodeSmellId,
										new CommitterBadCodeSmell(r.getChange().getCommit().getEmailCommitter(), badCodeSmellId,
												r.getChange().getCommit().getCommitter(), b));
							}
						}
					}
				}
			}
		}

	}

	public HashMap<String, CommitterBadCodeSmell> getAssociations() {
		return associations;
	}

	public void setAssociations(HashMap<String, CommitterBadCodeSmell> associations) {
		this.associations = associations;
	}

	public HashMap<String, Committer> getCommitters() {
		return committers;
	}

	public void setCommitters(HashMap<String, Committer> committers) {
		this.committers = committers;
	}

	public HashSet<BadCodeSmell> getBadCodeSmells() {
		return badCodeSmells;
	}

	public void setBadCodeSmells(HashSet<BadCodeSmell> badCodeSmells) {
		this.badCodeSmells = badCodeSmells;
	}

	private String getTypeBadCodeSmell(BadCodeSmell badCodeSmell) {

		if (badCodeSmell instanceof DeadCode)
			return badCodeSmell.getType();
		else if (badCodeSmell instanceof DuplicatedCode)
			return "DuplicatedCode";
		else if (badCodeSmell instanceof LargeClass)
			return "LongClass";
		else if (badCodeSmell instanceof LongMethod)
			return "LongMethod";
		else if (badCodeSmell instanceof LongParameterList)
			return "LongParametersList";
		else
			return "undefined";
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
	
}