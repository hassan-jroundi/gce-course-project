import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule, BreadcrumbModule, ButtonModule, CalendarModule, CheckboxModule, ConfirmDialogModule, DialogModule, DropdownModule, FieldsetModule, InputTextModule, ListboxModule, MessageModule, MultiSelectModule, PanelModule, ProgressSpinnerModule, TooltipModule, TreeModule } from 'primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [



];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    FieldsetModule,
    InputTextModule,
    PanelModule,
    TreeModule
  ],
  declarations: [

  ],
  exports: [
    RouterModule,
  ]
})
export class GceModule { }
