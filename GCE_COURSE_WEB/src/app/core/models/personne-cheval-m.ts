import { Personne } from './personne';
import { Cheval } from "./cheval";

export class PersonneChevalM{
    Id : string;
    cheval: Cheval;
    personne: Personne;
    codeNatureRelation: string;
    dateDebut: Date;
    dateFin: Date;

}