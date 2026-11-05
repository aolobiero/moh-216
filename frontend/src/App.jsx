import { clearApiCredentials } from "./services/api";
import StaffLogin from "./components/StaffLogin";
import Mothers from "./pages/Mothers";
import { useState } from "react";
import {
  LayoutDashboard,
  Users,
  Baby,
  Syringe,
  CalendarDays,
  HeartPulse,
  FileHeart,
  BarChart3,
  Settings,
  ShieldCheck,
  ArrowRight,
  Bell,
  Plus,
} from "lucide-react";

import "./App.css";

const menuItems = [
  { label: "Dashboard", icon: LayoutDashboard },
  { label: "Mothers", icon: Users },
  { label: "Children", icon: Baby },
  { label: "Immunizations", icon: Syringe },
  { label: "Appointments", icon: CalendarDays },
  { label: "Health Records", icon: FileHeart },
  { label: "Reports", icon: BarChart3 },
  { label: "Settings", icon: Settings },
];

const statistics = [
  { title: "Registered Mothers", value: "0", icon: Users },
  { title: "Registered Children", value: "0", icon: Baby },
  { title: "Appointments", value: "0", icon: CalendarDays },
  { title: "Immunizations", value: "0", icon: Syringe },
];

export default function App() {
  const [activePage, setActivePage] = useState("Dashboard");
  const [staffUser, setStaffUser] = useState(null);

  function handleLogout() {
    clearApiCredentials();
    setStaffUser(null);
    setActivePage("Dashboard");
  }

  const actions = [
    { label: "Register Mother", page: "Mothers", icon: Users },
    { label: "Register Child", page: "Children", icon: Baby },
    { label: "Add Immunization", page: "Immunizations", icon: Syringe },
    { label: "New Appointment", page: "Appointments", icon: CalendarDays },
  ];

  if (!staffUser) {
    return <StaffLogin onLogin={setStaffUser} />;
  }

  return (
    <div className="app-layout">
      <aside className="sidebar">
        <div className="logo">
          <div className="logo-icon">
            <HeartPulse size={28} />
          </div>

          <div>
            <h2>MOH 216</h2>
            <p>Mother & Child Health</p>
          </div>
        </div>

        <p className="menu-heading">MAIN MENU</p>

        <nav className="menu">
          {menuItems.map(({ label, icon: Icon }) => (
            <button
              key={label}
              className={activePage === label ? "selected" : ""}
              onClick={() => setActivePage(label)}
            >
              <Icon size={19} />
              <span>{label}</span>
            </button>
          ))}
        </nav>

        <div className="sidebar-bottom">
          <ShieldCheck size={24} />
          <div>
            <strong>Better Healthcare</strong>
            <p>For mothers and children</p>
          </div>
        </div>
      </aside>

      <main className="main-area">
        <header className="header">
          <div>
            <p className="header-label">MOH 216 PLATFORM</p>
            <h2>{activePage}</h2>
          </div>

          <div className="header-right">
            <Bell size={21} />
            <div className="admin-avatar">A</div>
            <span>{staffUser}</span>
            <button
              type="button"
              onClick={handleLogout}
              style={{
                marginLeft: "12px",
                padding: "8px 14px",
                borderRadius: "8px",
                border: "1px solid #cbd5e1",
                background: "#ffffff",
                cursor: "pointer"
              }}
            >
              Logout
            </button>
          </div>
        </header>

        <div className="dashboard-content">
          {activePage === "Dashboard" ? (
            <>
              <section className="welcome-banner">
                <div>
                  <span className="welcome-tag">
                    MOTHER & CHILD HEALTH HANDBOOK
                  </span>

                  <h1>Welcome to MOH 216</h1>

                  <p>
                    Supporting healthy mothers, healthy children,
                    and stronger communities through digital healthcare.
                  </p>

                  <button
                    className="primary-button"
                    onClick={() => setActivePage("Mothers")}
                  >
                    <Plus size={18} />
                    Register Mother
                  </button>
                </div>

                <div className="banner-icon">
                  <HeartPulse size={100} strokeWidth={1.2} />
                </div>
              </section>

              <section className="stats-grid">
                {statistics.map(({ title, value, icon: Icon }) => (
                  <div className="stat-card" key={title}>
                    <div className="stat-icon">
                      <Icon size={25} />
                    </div>

                    <p>{title}</p>
                    <h2>{value}</h2>
                  </div>
                ))}
              </section>

              <section className="dashboard-bottom">
                <div className="content-card">
                  <div className="card-heading">
                    <h2>Mother & Child Healthcare</h2>
                    <HeartPulse size={24} />
                  </div>

                  <p className="card-description">
                    Manage maternal healthcare records, antenatal visits,
                    child growth monitoring, immunizations and clinic
                    appointments in one digital platform.
                  </p>

                  <div className="feature-grid">
                    <div>
                      <HeartPulse size={30} />
                      <h3>Maternal Care</h3>
                      <p>Track pregnancy and antenatal visits.</p>
                    </div>

                    <div>
                      <Baby size={30} />
                      <h3>Child Health</h3>
                      <p>Monitor child growth and development.</p>
                    </div>

                    <div>
                      <ShieldCheck size={30} />
                      <h3>Health Records</h3>
                      <p>Organize important healthcare information.</p>
                    </div>
                  </div>
                </div>

                <div className="content-card quick-actions">
                  <h2>Quick Actions</h2>
                  <p>Get started with common tasks.</p>

                  {actions.map(({ label, page, icon: Icon }) => (
                    <button
                      key={label}
                      onClick={() => setActivePage(page)}
                    >
                      <span className="quick-icon">
                        <Icon size={20} />
                      </span>

                      <span>{label}</span>
                      <ArrowRight size={18} />
                    </button>
                  ))}
                </div>
              </section>
            </>
          ) : activePage === "Mothers" ? (
            <Mothers />
          ) : (
            <section className="content-card module-placeholder">
              <h2>{activePage}</h2>

              <p>
                The {activePage.toLowerCase()} module will be
                implemented in the next development phase.
              </p>

              <button
                className="primary-button"
                onClick={() => setActivePage("Dashboard")}
              >
                Return to Dashboard
              </button>
            </section>
          )}
        </div>
      </main>
    </div>
  );
}
