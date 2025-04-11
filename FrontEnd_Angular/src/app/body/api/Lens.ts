import { LensManufacturer } from "./LensManufacturer";

export interface Lens {
    id: number;
    createdBy: string;
    createdAt: string;
    modifiedBy: string;
    modifiedAt: string;
    deleted: boolean;
    version: number;

    name: string | null;
    commentTradeName: string | null;
    nominal: number | null;
    haigisA0: number | null;
    haigisA1: number | null;
    haigisA2: number | null;
    hofferQPACD: number | null;
    holladay1SF: number | null;
    srkta: number | null;
    castropC: number | null;
    castropH: number | null;
    castropR: number | null;
    haigisA0Optimized: number | null;
    haigisA1Optimized: number | null;
    haigisA2Optimized: number | null;
    hofferQPACDOptimized: number | null;
    holladay1SFOptimized: number | null;
    cookeOptimized: number | null;
    srktaOptimized: number | null;
    castropCOptimized: number | null;
    castropHOptimized: number | null;
    castropROptimized: number | null;
    lensManufacturer: LensManufacturer | null;
}
