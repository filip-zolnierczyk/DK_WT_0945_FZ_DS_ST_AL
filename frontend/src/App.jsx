import DoctorList from "./components/DoctorList.jsx";
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AddDoctorForm from './components/AddDoctorForm.jsx';
import Layout from './Layout.jsx';
import DoctorDetails from "./components/DoctorDetails.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<Layout/>}>
                    <Route
                        index
                        element={
                            <div className="flex flex-col items-center gap-6 w-full">
                                <DoctorList/>
                            </div>
                        }
                    />
                    <Route path="/add-doctor" element={<AddDoctorForm/>}/>
                    <Route path="/doctors/:id" element={<DoctorDetails/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
