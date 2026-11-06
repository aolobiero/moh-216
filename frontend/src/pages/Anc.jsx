import { useEffect, useState } from "react";
import { ArrowLeft, CalendarDays, Plus } from "lucide-react";
import { getAncPregnancies, getAncVisits, getMothers, createPregnancy, createAncVisit } from "../services/api";
import "./Anc.css";

const emptyPregnancy = {
  motherId: "",
  lastMenstrualPeriod: "",
  expectedDeliveryDate: "",
  gravida: "",
  parity: "",
};

const emptyVisit = {
  visitDate: "",
  weightKg: "",
  systolicBp: "",
  diastolicBp: "",
  clinicalNotes: "",
  followUpDate: "",
};

export default function Anc() {
  const [mothers, setMothers] = useState([]);
  const [pregnancies, setPregnancies] = useState([]);
  const [selectedPregnancy, setSelectedPregnancy] = useState(null);
  const [visits, setVisits] = useState([]);
  const [pregnancyForm, setPregnancyForm] = useState(emptyPregnancy);
  const [visitForm, setVisitForm] = useState(emptyVisit);
  const [showPregnancyForm, setShowPregnancyForm] = useState(false);
  const [showVisitForm, setShowVisitForm] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    getMothers().then(setMothers).catch((requestError) => {
      console.error("Failed to load mothers for ANC:", requestError);
      setError("Mother records could not be loaded.");
    });
  }, []);

  async function loadPregnancies(motherId) {
    if (!motherId) {
      setPregnancies([]);
      return;
    }
    try {
      setPregnancies(await getAncPregnancies(motherId));
    } catch (requestError) {
      console.error("Failed to load pregnancies:", requestError);
      setError("Pregnancy records could not be loaded.");
    }
  }

  async function selectPregnancy(pregnancy) {
    setSelectedPregnancy(pregnancy);
    setShowVisitForm(false);
    try {
      setVisits(await getAncVisits(pregnancy.id));
    } catch (requestError) {
      console.error("Failed to load ANC visits:", requestError);
      setError("ANC visits could not be loaded.");
    }
  }

  function updateForm(setter) {
    return (event) => setter((previous) => ({ ...previous, [event.target.name]: event.target.value }));
  }

  async function submitPregnancy(event) {
    event.preventDefault();
    setError("");
    setSuccess("");
    try {
      await createPregnancy({
        ...pregnancyForm,
        motherId: Number(pregnancyForm.motherId),
        gravida: pregnancyForm.gravida === "" ? null : Number(pregnancyForm.gravida),
        parity: pregnancyForm.parity === "" ? null : Number(pregnancyForm.parity),
        lastMenstrualPeriod: pregnancyForm.lastMenstrualPeriod || null,
        expectedDeliveryDate: pregnancyForm.expectedDeliveryDate || null,
      });
      await loadPregnancies(pregnancyForm.motherId);
      setPregnancyForm(emptyPregnancy);
      setShowPregnancyForm(false);
      setSuccess("Pregnancy record created.");
    } catch (requestError) {
      console.error("Failed to create pregnancy:", requestError);
      setError(requestError.message || "Pregnancy record could not be created.");
    }
  }

  async function submitVisit(event) {
    event.preventDefault();
    setError("");
    setSuccess("");
    try {
      await createAncVisit(selectedPregnancy.id, {
        ...visitForm,
        weightKg: visitForm.weightKg ? Number(visitForm.weightKg) : null,
        systolicBp: visitForm.systolicBp ? Number(visitForm.systolicBp) : null,
        diastolicBp: visitForm.diastolicBp ? Number(visitForm.diastolicBp) : null,
        followUpDate: visitForm.followUpDate || null,
      });
      setVisits(await getAncVisits(selectedPregnancy.id));
      setVisitForm(emptyVisit);
      setShowVisitForm(false);
      setSuccess("ANC visit recorded.");
    } catch (requestError) {
      console.error("Failed to record ANC visit:", requestError);
      setError(requestError.message || "ANC visit could not be recorded.");
    }
  }

  return (
    <div className="anc-page">
      <div className="anc-heading">
        <div><h1>Antenatal Care</h1><p>Record pregnancy history and ANC follow-up visits.</p></div>
        <button className="anc-primary-btn" onClick={() => setShowPregnancyForm(!showPregnancyForm)}>
          {showPregnancyForm ? <ArrowLeft size={18} /> : <Plus size={18} />}
          {showPregnancyForm ? "Back to ANC" : "Add Pregnancy"}
        </button>
      </div>
      {error && <p className="anc-message error" role="alert">{error}</p>}
      {success && <p className="anc-message success" role="status">{success}</p>}

      {showPregnancyForm ? (
        <form className="anc-card anc-form" onSubmit={submitPregnancy}>
          <h2>Pregnancy record</h2>
          <label>Mother *
            <select name="motherId" value={pregnancyForm.motherId} onChange={updateForm(setPregnancyForm)} required>
              <option value="">Select mother</option>
              {mothers.map((mother) => <option key={mother.id} value={mother.id}>{mother.fullName}</option>)}
            </select>
          </label>
          <div className="anc-form-grid">
            <label>Last menstrual period<input type="date" name="lastMenstrualPeriod" value={pregnancyForm.lastMenstrualPeriod} onChange={updateForm(setPregnancyForm)} /></label>
            <label>Expected delivery date<input type="date" name="expectedDeliveryDate" value={pregnancyForm.expectedDeliveryDate} onChange={updateForm(setPregnancyForm)} /></label>
            <label>Gravida<input type="number" min="0" name="gravida" value={pregnancyForm.gravida} onChange={updateForm(setPregnancyForm)} /></label>
            <label>Parity<input type="number" min="0" name="parity" value={pregnancyForm.parity} onChange={updateForm(setPregnancyForm)} /></label>
          </div>
          <div className="anc-actions"><button type="submit" className="anc-primary-btn">Save Pregnancy</button></div>
        </form>
      ) : selectedPregnancy ? (
        <section className="anc-card">
          <button className="anc-back-link" onClick={() => setSelectedPregnancy(null)}><ArrowLeft size={17} /> Back to pregnancies</button>
          <div className="anc-detail-heading">
            <div><h2>{selectedPregnancy.mother?.fullName || "Pregnancy record"}</h2><p>Pregnancy #{selectedPregnancy.id}</p></div>
            <button className="anc-primary-btn" onClick={() => setShowVisitForm(!showVisitForm)}><Plus size={18} /> Record ANC visit</button>
          </div>
          <div className="anc-summary">
            <span>LMP: {selectedPregnancy.lastMenstrualPeriod || "Not recorded"}</span>
            <span>EDD: {selectedPregnancy.expectedDeliveryDate || "Not recorded"}</span>
            <span>Gravida: {selectedPregnancy.gravida ?? "Not recorded"}</span>
            <span>Parity: {selectedPregnancy.parity ?? "Not recorded"}</span>
          </div>
          {showVisitForm && (
            <form className="anc-visit-form" onSubmit={submitVisit}>
              <h3>ANC visit</h3>
              <div className="anc-form-grid">
                <label>Visit date *<input type="date" name="visitDate" value={visitForm.visitDate} onChange={updateForm(setVisitForm)} required /></label>
                <label>Weight (kg)<input type="number" min="0.01" step="0.01" name="weightKg" value={visitForm.weightKg} onChange={updateForm(setVisitForm)} /></label>
                <label>Systolic BP<input type="number" min="1" name="systolicBp" value={visitForm.systolicBp} onChange={updateForm(setVisitForm)} /></label>
                <label>Diastolic BP<input type="number" min="1" name="diastolicBp" value={visitForm.diastolicBp} onChange={updateForm(setVisitForm)} /></label>
                <label>Follow-up date<input type="date" name="followUpDate" value={visitForm.followUpDate} onChange={updateForm(setVisitForm)} /></label>
              </div>
              <label>Clinical notes<textarea name="clinicalNotes" rows="4" value={visitForm.clinicalNotes} onChange={updateForm(setVisitForm)} /></label>
              <div className="anc-actions"><button type="submit" className="anc-primary-btn">Save visit</button></div>
            </form>
          )}
          <h3 className="anc-visits-title">Visit history</h3>
          {visits.length === 0 ? <p className="anc-empty">No ANC visits recorded.</p> : (
            <div className="anc-table-wrapper"><table><thead><tr><th>Date</th><th>Weight</th><th>Blood pressure</th><th>Follow-up</th><th>Notes</th></tr></thead><tbody>
              {visits.map((visit) => <tr key={visit.id}><td>{visit.visitDate}</td><td>{visit.weightKg ? `${visit.weightKg} kg` : "—"}</td><td>{visit.systolicBp && visit.diastolicBp ? `${visit.systolicBp}/${visit.diastolicBp}` : "—"}</td><td>{visit.followUpDate || "—"}</td><td>{visit.clinicalNotes || "—"}</td></tr>)}
            </tbody></table></div>
          )}
        </section>
      ) : (
        <section className="anc-card">
          <div className="anc-filter"><label>Mother<select value={pregnancyForm.motherId} onChange={(event) => { setPregnancyForm((previous) => ({ ...previous, motherId: event.target.value })); loadPregnancies(event.target.value); }}><option value="">Select mother</option>{mothers.map((mother) => <option key={mother.id} value={mother.id}>{mother.fullName}</option>)}</select></label></div>
          {pregnancies.length === 0 ? <div className="anc-empty"><CalendarDays size={48} /><h3>No pregnancy records</h3><p>Select a mother or add a pregnancy record.</p></div> : <div className="anc-list">{pregnancies.map((pregnancy) => <button key={pregnancy.id} className="anc-list-item" onClick={() => selectPregnancy(pregnancy)}><strong>Pregnancy #{pregnancy.id}</strong><span>EDD: {pregnancy.expectedDeliveryDate || "Not recorded"}</span><span>Recorded: {pregnancy.recordedAt ? new Date(pregnancy.recordedAt).toLocaleDateString() : "—"}</span></button>)}</div>}
        </section>
      )}
    </div>
  );
}
