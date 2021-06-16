import { ExportGroupGenerationGQL } from "src/generated/graphql";
import { downloadBlob, base64ToBlob } from "../utilities"

export function downloadGroupGeneration(exportGroupGenerationGql: ExportGroupGenerationGQL, element: HTMLElement, groupGenerationId?: number) {
  exportGroupGenerationGql.fetch({groupGenerationId})
  .subscribe(res => {
    if (res.data.exportGroupGeneration) {
      let blob = base64ToBlob(res.data.exportGroupGeneration, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
      if (blob) {
        downloadBlob('グループ構成', blob, element);
      }
    }
  })

}
