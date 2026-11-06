import { useEffect, useState } from "react";
import { Bell, Building2, Check, Moon, Palette, Save, ShieldCheck, UserRound } from "lucide-react";
import {
  getFacilitySettings, getSettingsPreferences, getSettingsProfile,
  saveFacilitySettings, saveSettingsPreferences, saveSettingsProfile,
} from "../services/api";
import "./Settings.css";

const blankProfile = { fullName: "", email: "", phone: "", designation: "" };
const blankFacility = { facilityName: "", mflCode: "", county: "", subCounty: "", location: "", contactPhone: "", contactEmail: "", administratorName: "" };
const blankPreferences = { theme: "light", textSize: "normal", language: "en", ancReminders: true, immunizationReminders: true, pncReminders: true, missedAppointmentNotifications: true };
const themeOptions = [
  ["light", "Light", "Current pink-and-white design"],
  ["midnight-navy", "Midnight Navy", "Deep blue with pink accents"],
  ["dusty-plum", "Dusty Plum", "Recommended premium dark theme"],
  ["rose-charcoal", "Rose Charcoal", "Warm charcoal with rose accents"],
];

export default function SettingsPage({ staffUser }) {
  const [section, setSection] = useState("profile");
  const [profile, setProfile] = useState(blankProfile);
  const [facility, setFacility] = useState(blankFacility);
  const [preferences, setPreferences] = useState(blankPreferences);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    let stored = {};
    try {
      stored = JSON.parse(localStorage.getItem("moh216-preferences") || "{}");
    } catch (storageError) {
      console.warn("Ignoring invalid saved theme preferences:", storageError);
    }
    setPreferences((previous) => ({ ...previous, ...stored }));
    Promise.allSettled([getSettingsProfile(), getSettingsPreferences(), getFacilitySettings()])
      .then(([profileResult, preferencesResult, facilityResult]) => {
        if (profileResult.status === "fulfilled") setProfile({ ...blankProfile, ...profileResult.value });
        if (preferencesResult.status === "fulfilled") setPreferences((previous) => ({ ...previous, ...preferencesResult.value }));
        if (facilityResult.status === "fulfilled") setFacility({ ...blankFacility, ...facilityResult.value });
        const failures = [profileResult, preferencesResult, facilityResult].filter((result) => result.status === "rejected");
        if (failures.length > 0) {
          console.error("Settings requests failed:", failures.map((result) => result.reason));
          setError("Some settings are unavailable. Apply migration 007 and sign in with an administrator for facility settings.");
        }
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    document.documentElement.dataset.theme = preferences.theme || "light";
    document.documentElement.dataset.textSize = preferences.textSize;
    localStorage.setItem("moh216-preferences", JSON.stringify({ theme: preferences.theme, textSize: preferences.textSize }));
  }, [preferences.theme, preferences.textSize]);

  function update(setter) {
    return (event) => setter((previous) => ({ ...previous, [event.target.name]: event.target.value }));
  }

  function toggle(name) {
    setPreferences((previous) => ({ ...previous, [name]: !previous[name] }));
  }

  async function save() {
    setSaving(true); setMessage(""); setError("");
    try {
      if (section === "profile") await saveSettingsProfile(profile);
      if (section === "facility") await saveFacilitySettings(facility);
      if (section === "notifications" || section === "appearance") await saveSettingsPreferences(preferences);
      setMessage("Settings saved successfully.");
    } catch (requestError) {
      console.error("Failed to save settings:", requestError);
      setError(requestError.message || "Settings could not be saved.");
    } finally { setSaving(false); }
  }

  const nav = [
    ["profile", UserRound, "My profile"],
    ["facility", Building2, "Facility"],
    ["notifications", Bell, "Notifications"],
    ["appearance", Palette, "Appearance"],
  ];

  return (
    <div className="settings-page">
      <div className="settings-intro"><div><h1>Settings</h1><p>Manage your account, facility preferences, and privacy controls.</p></div><span className="settings-account"><ShieldCheck size={17} /> {staffUser}</span></div>
      {error && <p className="settings-message error" role="alert">{error}</p>}
      {message && <p className="settings-message success" role="status"><Check size={16} /> {message}</p>}
      <div className="settings-layout">
        <nav className="settings-nav" aria-label="Settings sections">
          {nav.map(([key, Icon, label]) => <button key={key} className={section === key ? "active" : ""} onClick={() => { setSection(key); setMessage(""); }}><Icon size={18} />{label}</button>)}
        </nav>
        <section className="settings-panel">
          {loading ? <p className="settings-loading">Loading settings...</p> : (
            <>
              {section === "profile" && <ProfileForm profile={profile} update={update(setProfile)} />}
              {section === "facility" && <FacilityForm facility={facility} update={update(setFacility)} />}
              {section === "notifications" && <NotificationForm preferences={preferences} toggle={toggle} />}
              {section === "appearance" && <AppearanceForm preferences={preferences} update={update(setPreferences)} />}
              <div className="settings-actions"><button className="settings-save" onClick={save} disabled={saving}><Save size={17} />{saving ? "Saving..." : "Save changes"}</button></div>
            </>
          )}
        </section>
      </div>
    </div>
  );
}

function ProfileForm({ profile, update }) {
  return <div><h2>My profile</h2><p className="settings-help">Your profile is linked to the authenticated staff username. Password changes require the future staff identity service and are not enabled for the generated development account.</p><div className="settings-form-grid"><Field label="Username" value={profile.username || "Authenticated user"} disabled /><Field label="Full name" name="fullName" value={profile.fullName} onChange={update} /><Field label="Email" name="email" type="email" value={profile.email} onChange={update} /><Field label="Phone" name="phone" value={profile.phone} onChange={update} /><Field label="Professional designation" name="designation" value={profile.designation} onChange={update} /></div></div>;
}
function FacilityForm({ facility, update }) {
  return <div><h2>Healthcare facility</h2><p className="settings-help">Only administrators can change facility information. Official MFL codes should be entered from the facility registry.</p><div className="settings-form-grid"><Field label="Facility name" name="facilityName" value={facility.facilityName} onChange={update} /><Field label="Official MFL code" name="mflCode" value={facility.mflCode} onChange={update} /><Field label="County" name="county" value={facility.county} onChange={update} /><Field label="Sub-county" name="subCounty" value={facility.subCounty} onChange={update} /><Field label="Location" name="location" value={facility.location} onChange={update} /><Field label="Contact phone" name="contactPhone" value={facility.contactPhone} onChange={update} /><Field label="Contact email" name="contactEmail" type="email" value={facility.contactEmail} onChange={update} /><Field label="Facility administrator" name="administratorName" value={facility.administratorName} onChange={update} /></div></div>;
}
function NotificationForm({ preferences, toggle }) {
  const items = [["ancReminders", "ANC appointment reminders"], ["immunizationReminders", "Immunization reminders"], ["pncReminders", "Postnatal follow-up reminders"], ["missedAppointmentNotifications", "Missed appointment notifications"]];
  return <div><h2>Notifications and reminders</h2><p className="settings-help">These preferences control in-app reminders only. SMS and email delivery are not configured.</p><div className="settings-toggles">{items.map(([name, label]) => <label key={name} className="settings-toggle"><span>{label}</span><input type="checkbox" checked={preferences[name]} onChange={() => toggle(name)} /><span className="toggle-track" /></label>)}</div></div>;
}
function AppearanceForm({ preferences, update }) {
  return <div><h2>Appearance and accessibility</h2><p className="settings-help">Themes apply immediately and are saved to your account when you save changes.</p><div className="theme-options">{themeOptions.map(([value, label, description]) => <label key={value} className={`theme-option ${preferences.theme === value ? "selected" : ""}`}><input type="radio" name="theme" value={value} checked={preferences.theme === value} onChange={update} /><span><strong>{label}</strong><small>{description}</small></span></label>)}</div><div className="settings-form-grid"><label>Text size<select name="textSize" value={preferences.textSize} onChange={update}><option value="normal">Normal</option><option value="large">Large</option></select></label><label>Language<select name="language" value={preferences.language} onChange={update}><option value="en">English</option><option value="sw" disabled>Kiswahili (coming soon)</option></select></label></div><div className="appearance-note"><Moon size={18} /> Theme and text-size changes apply without refreshing.</div></div>;
}
function Field({ label, name, value, onChange, type = "text", disabled = false }) {
  return <label>{label}<input name={name} type={type} value={value || ""} onChange={onChange} disabled={disabled} /></label>;
}
