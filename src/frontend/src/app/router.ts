import { Routes } from "@angular/router";

export const routes: Routes = [
    {
    
        path: "",
        loadComponent: 
            ()=> import("./sessions-table/sessions-table.component")
            .then(c=> c.SessionsTableComponent),
       
    },

]