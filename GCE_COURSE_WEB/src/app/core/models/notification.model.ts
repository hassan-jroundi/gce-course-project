import { Cheval } from "./cheval";

export class notification {
    constructor(id: string, message: string, titre: string, createdOn: Date, type: string, groupeUtilisateur: string, cheval: Cheval, dejaLu: boolean) {
        this.id = id;
        this.message = message;
        this.titre = titre;
        this.createdOn = createdOn;
        this.type = type;
        this.groupeUtilisateur = groupeUtilisateur;
        this.cheval = cheval;
        this.dejaLu = dejaLu;
    }
    id: string;
    message: string;
    titre: string;
    createdOn: Date;
    createdBy: number;
    type: string;
    groupeUtilisateur: string;
    cheval: Cheval;
    dejaLu: boolean;
}
