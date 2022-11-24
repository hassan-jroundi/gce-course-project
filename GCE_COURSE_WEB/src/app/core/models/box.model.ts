import { Ecurie } from './ecurie.model';
import { Site } from './site.model';
export class Box {
    id: number;
    nom: string;
    ecurie: Ecurie;
    prix: string;
    isActif: boolean;
    prixUnitaire: string;
    prixForfaitaire: string;
}