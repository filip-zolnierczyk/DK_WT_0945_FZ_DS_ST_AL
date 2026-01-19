import DoctorList from "./components/DoctorList.jsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AddDoctorForm from "./components/AddDoctorForm.jsx";
import AddPatientForm from "./components/AddPatientForm.jsx";
import Layout from "./Layout.jsx";
import DoctorDetails from "./components/DoctorDetails.jsx";
import PatientList from "./components/PatientList.jsx";
import OfficeList from "./components/OfficeList.jsx";
import AddOfficeForm from "./components/AddOfficeForm.jsx";
import AddDutyForm from "./components/AddDutyForm.jsx";
import OfficeDetails from "./components/OfficeDetails.jsx";
import BookAppointment from "./components/BookAppointment.jsx";
import PatientDetails from "./components/PatientDetails.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route
            index
            element={
              <div className="flex flex-col items-center gap-6 w-full">
                <DoctorList />
              </div>
            }
          />
          <Route path="/add-doctor" element={<AddDoctorForm />} />
          <Route path="/doctors/:id" element={<DoctorDetails />} />
          <Route path="/offices/:id/duties" element={<OfficeDetails />} />
          <Route path="/patients" element={<PatientList />} />
          <Route path="/add-patient" element={<AddPatientForm />} />
          <Route path="/offices" element={<OfficeList />} />
          <Route path="/add-office" element={<AddOfficeForm />} />
          <Route path="/add-duty" element={<AddDutyForm />} />
          <Route
            path="/patients/:patientId/book"
            element={<BookAppointment />}
          />
          <Route path="/patients/:patientId" element={<PatientDetails />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
