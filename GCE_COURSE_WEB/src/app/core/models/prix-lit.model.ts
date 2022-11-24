import { Lit } from './lit.model';
import { Box } from './box.model';

export class PrixLit {
    id: number;
    lit: Lit;
    montant: number;
    dateDebut: Date;
    dateFin: Date;
}