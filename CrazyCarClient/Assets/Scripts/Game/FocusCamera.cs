using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using QFramework;
using UnityEngine.ResourceManagement.AsyncOperations;
using Utils;

public class FocusCamera : MonoBehaviour, IController {
	public new Camera camera;
	public Vector3 cameraDelta = new Vector3(0f, 1300f, 0f);

	// Use this for initialization
	void Start () {
		this.GetSystem<IAddressableSystem>().LoadAssetAsync<RenderTexture>(Util.miniMapPath, (obj) => {
			if (obj.Status == AsyncOperationStatus.Succeeded) {
				camera.targetTexture = obj.Result;
			} else {
				Debug.LogError($"Load minimap Failed");
			}
		}).Forget();
	}
	
	// Update is called once per frame
	void LateUpdate () {
		MPlayer bike = this.GetSystem<IPlayerManagerSystem>().SelfPlayer;
		if (bike != null) {
			camera.transform.position = bike.transform.position + cameraDelta;
		}
	}

    public IArchitecture GetArchitecture() {
		return CrazyCar.Interface;
    }
}
