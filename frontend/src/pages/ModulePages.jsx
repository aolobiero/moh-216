import { useEffect, useState } from "react";
import { createAppointment, createHealthRecord, createImmunization, getAppointments, getChildren, getHealthRecords, getImmunizations, getMothers, getReportSummary } from "../services/api";
import "./ModulePages.css";

function Message({ error, success }) {
  return <>{error && <p className="module-message error" role="alert">{error}</p>}{success && <p className="module-message success" role="status">{success}</p>}</>;
}
function useDirectory() {
  const [mothers, setMothers] = useState([]); const [children, setChildren] = useState([]);
  useEffect(() => { Promise.all([getMothers(), getChildren()]).then(([m, c]) => { setMothers(m); setChildren(c); }).catch(() => {}); }, []);
  return { mothers, children };
}

const vaccineCatalogue = [
  "BCG",
  "Oral Polio Vaccine (OPV)",
  "Inactivated Polio Vaccine (IPV)",
  "Pentavalent vaccine",
  "Pneumococcal Conjugate Vaccine (PCV)",
  "Rotavirus vaccine",
  "Measles-Rubella (MR)",
  "Yellow Fever vaccine",
  "Human Papillomavirus (HPV) vaccine",
];

export function HealthRecords() {
  const { mothers, children } = useDirectory(); const [subject, setSubject] = useState({ type: "mother", id: "" });
  const [patientSearch, setPatientSearch] = useState("");
  const [records, setRecords] = useState([]); const [form, setForm] = useState({ recordType: "Clinical consultation", title: "", recordDate: "", clinicalNotes: "", sensitive: false });
  const [error, setError] = useState(""); const [success, setSuccess] = useState("");
  async function load(value = subject) { if (!value.id) return setRecords([]); try { setRecords(await getHealthRecords(value)); } catch (e) { setError(e.message); } }
  function change(e) { setForm({ ...form, [e.target.name]: e.target.type === "checkbox" ? e.target.checked : e.target.value }); }
  async function submit(e) { e.preventDefault(); setError(""); try { await createHealthRecord({ ...form, recordDate: form.recordDate || null, motherId: subject.type === "mother" ? Number(subject.id) : null, childId: subject.type === "child" ? Number(subject.id) : null }); await load(); setSuccess("Health record saved."); setForm({ ...form, title: "", clinicalNotes: "" }); } catch (err) { setError(err.message); } }
  const patients = (subject.type === "mother" ? mothers : children).filter((patient) => {
    const query = patientSearch.trim().toLowerCase();
    return !query || `${patient.fullName} ${patient.phoneNumber || ""} ${patient.motherName || ""}`.toLowerCase().includes(query);
  });
  return <div className="module-page"><div className="module-heading"><div><h1>Health Records</h1><p>Longitudinal records with dated authorship and subject linkage.</p></div></div><Message error={error} success={success} />
    <section className="module-card"><div className="module-grid"><label>Subject type<select value={subject.type} onChange={e => { setSubject({ type: e.target.value, id: "" }); setRecords([]); setPatientSearch(""); }}><option value="mother">Mother</option><option value="child">Child</option></select></label><label>Search patient<input value={patientSearch} onChange={e => setPatientSearch(e.target.value)} placeholder="Name, phone, or linked mother" /></label><label>Patient<select value={subject.id} onChange={e => { const next = { ...subject, id: e.target.value }; setSubject(next); load(next); }}><option value="">Select patient</option>{patients.map(p => <option key={p.id} value={p.id}>{p.fullName}{p.motherName ? ` — mother: ${p.motherName}` : ""}</option>)}</select></label></div></section>
    <form className="module-card module-form" onSubmit={submit}><h2>Add clinical entry</h2><div className="module-grid"><label>Record type<select name="recordType" value={form.recordType} onChange={change}><option>Clinical consultation</option><option>Laboratory investigation</option><option>Medication</option><option>Referral</option><option>Admission</option><option>Other</option></select></label><label>Title<input name="title" value={form.title} onChange={change} required /></label><label>Record date<input name="recordDate" type="date" value={form.recordDate} onChange={change} required /></label></div><label>Clinical notes<textarea name="clinicalNotes" rows="4" value={form.clinicalNotes} onChange={change} /></label><label className="check-label"><input type="checkbox" name="sensitive" checked={form.sensitive} onChange={change} /> Restricted clinical entry</label><button className="module-primary" disabled={!subject.id}>Save record</button></form>
    <section className="module-card"><h2>History</h2>{records.length ? <div className="module-list">{records.map(r => <article key={r.id}><strong>{r.title}</strong><span>{r.recordType} · {r.recordDate} · {r.authorUsername}</span><p>{r.clinicalNotes || "No notes recorded."}</p></article>)}</div> : <p className="module-empty">Select a patient to view their record history.</p>}</section>
  </div>;
}

export function Appointments() {
  const { mothers, children } = useDirectory(); const [items, setItems] = useState([]); const [error, setError] = useState(""); const [success, setSuccess] = useState("");
  const now = new Date(); const start = new Date(now.getFullYear(), now.getMonth(), 1).toISOString(); const end = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59).toISOString();
  const [form, setForm] = useState({ subjectType: "mother", subjectId: "", appointmentType: "ANC", startsAt: "", facilityName: "", notes: "" });
  async function load() { try { setItems(await getAppointments(start, end)); } catch (e) { setError(e.message); } }
  useEffect(() => { load(); }, []);
  async function submit(e) { e.preventDefault(); try { await createAppointment({ motherId: form.subjectType === "mother" ? Number(form.subjectId) : null, childId: form.subjectType === "child" ? Number(form.subjectId) : null, appointmentType: form.appointmentType, startsAt: form.startsAt, facilityName: form.facilityName, notes: form.notes }); await load(); setSuccess("Appointment booked."); } catch (err) { setError(err.message); } }
  const people = form.subjectType === "mother" ? mothers : children;
  return <div className="module-page"><div className="module-heading"><div><h1>Appointments</h1><p>Schedule and review upcoming care visits.</p></div></div><Message error={error} success={success} /><form className="module-card module-form" onSubmit={submit}><h2>Book appointment</h2><div className="module-grid"><label>For<select value={form.subjectType} onChange={e => setForm({ ...form, subjectType: e.target.value, subjectId: "" })}><option value="mother">Mother</option><option value="child">Child</option></select></label><label>Patient<select value={form.subjectId} onChange={e => setForm({ ...form, subjectId: e.target.value })} required><option value="">Select patient</option>{people.map(p => <option key={p.id} value={p.id}>{p.fullName}</option>)}</select></label><label>Appointment type<select value={form.appointmentType} onChange={e => setForm({ ...form, appointmentType: e.target.value })}><option>ANC</option><option>PNC</option><option>Immunization</option><option>Growth monitoring</option><option>Consultation</option><option>Follow-up</option></select></label><label>Date and time<input type="datetime-local" value={form.startsAt} onChange={e => setForm({ ...form, startsAt: e.target.value })} required /></label><label>Facility<input value={form.facilityName} onChange={e => setForm({ ...form, facilityName: e.target.value })} /></label></div><label>Notes<textarea value={form.notes} onChange={e => setForm({ ...form, notes: e.target.value })} /></label><button className="module-primary">Book appointment</button></form><section className="module-card"><h2>This month</h2>{items.length ? <div className="module-list">{items.map(a => <article key={a.id}><strong>{a.appointmentType}</strong><span>{a.startsAt} · {a.status}</span><p>{a.mother?.fullName || a.child?.fullName || "Patient"}{a.facilityName ? ` · ${a.facilityName}` : ""}</p></article>)}</div> : <p className="module-empty">No appointments recorded for this month.</p>}</section></div>;
}

export function Immunizations() {
  const { children } = useDirectory(); const [childId, setChildId] = useState(""); const [items, setItems] = useState([]); const [form, setForm] = useState({ vaccineName: "", doseLabel: "", administeredDate: "", notes: "" }); const [error, setError] = useState(""); const [success, setSuccess] = useState("");
  async function load(id = childId) { if (!id) return setItems([]); try { setItems(await getImmunizations(id)); } catch (e) { setError(e.message); } }
  async function submit(e) { e.preventDefault(); try { await createImmunization({ ...form, childId: Number(childId) }); await load(); setSuccess("Administration record saved."); setForm({ ...form, vaccineName: "", doseLabel: "", notes: "" }); } catch (err) { setError(err.message); } }
  return <div className="module-page"><div className="module-heading"><div><h1>Immunizations</h1><p>Record administrations with a searchable vaccine catalogue.</p></div></div><Message error={error} success={success} /><section className="module-card"><label>Child<select value={childId} onChange={e => { setChildId(e.target.value); load(e.target.value); }}><option value="">Select child</option>{children.map(c => <option key={c.id} value={c.id}>{c.fullName}</option>)}</select></label></section><form className="module-card module-form" onSubmit={submit}><h2>Record administration</h2><div className="module-grid"><label>Vaccine name (searchable)<input list="moh216-vaccine-catalogue" value={form.vaccineName} onChange={e => setForm({ ...form, vaccineName: e.target.value })} placeholder="Search or select a vaccine" required /><datalist id="moh216-vaccine-catalogue">{vaccineCatalogue.map(vaccine => <option key={vaccine} value={vaccine} />)}</datalist></label><label>Dose label<input value={form.doseLabel} onChange={e => setForm({ ...form, doseLabel: e.target.value })} placeholder="Enter approved dose/visit label" /></label><label>Administration date<input type="date" value={form.administeredDate} onChange={e => setForm({ ...form, administeredDate: e.target.value })} required /></label></div><p className="module-note">Catalogue names are based on the vaccine types identified for Kenya programme review. Timing, dose count, eligibility, and regional requirements are not inferred here; confirm them against the current Kenya MoH/KEN-EPI schedule.</p><label>Notes<textarea value={form.notes} onChange={e => setForm({ ...form, notes: e.target.value })} /></label><button className="module-primary" disabled={!childId}>Save administration</button></form><section className="module-card"><h2>History</h2>{items.length ? <div className="module-list">{items.map(i => <article key={i.id}><strong>{i.vaccineName}</strong><span>{i.doseLabel || "Dose not labelled"} · {i.administeredDate}</span><p>{i.notes || "No notes recorded."}</p></article>)}</div> : <p className="module-empty">Select a child to view immunization history.</p>}</section></div>;
}

export function Reports() {
  const [summary, setSummary] = useState(null); const [error, setError] = useState("");
  useEffect(() => { getReportSummary().then(setSummary).catch(e => setError(e.message)); }, []);
  return <div className="module-page"><div className="module-heading"><div><h1>Reports & Analytics</h1><p>Operational aggregates only; patient exports require authorization and governance.</p></div></div><Message error={error} />{summary ? <div className="report-grid"><div className="report-card"><span>Scheduled appointments</span><strong>{summary.scheduledAppointments}</strong></div><div className="report-card"><span>Immunizations recorded</span><strong>{summary.immunizationsRecorded}</strong></div></div> : <section className="module-card">Loading report summary…</section>}<section className="module-card"><h2>Clinical governance</h2><p>Vaccination schedules, growth references, alerts, and identifiable exports remain disabled until current Kenyan guidance, access policy, and clinical review are approved.</p></section></div>;
}
