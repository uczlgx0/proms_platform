export class Category {
    constructor(public items?: CategoryItem[],
                public key?: string) {

    }
}

export class CategoryItem {
    constructor(public count?: number,
                public key?: string,
                public selected?: boolean) {
        this.selected = false;
    }
}
