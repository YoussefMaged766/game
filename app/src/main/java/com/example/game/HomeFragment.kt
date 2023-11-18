package com.example.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.game.databinding.FragmentHomeBinding
import com.example.game.db.GameEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val imageIds = intArrayOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5,
        R.drawable.img6,
        R.drawable.img7,
        R.drawable.img8,
        R.drawable.img9,
        R.drawable.img10
    )
    private val selectedIndices = mutableListOf<Int>()

    private var currentBet: Int = 1
    private val viewModel: StatusViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMenu()
        lifecycleScope.launch {
            viewModel.getCurrentAssetsFlow().collect {
                binding.txtAssets.text = "${requireActivity().getString(R.string.assets)} $it$"
            }
        }

        lifecycleScope.launch {
            viewModel.getCurrentImagesFlow().collect {
                binding.img1.setImageResource(it[0])
                binding.img2.setImageResource(it[1])
                binding.img3.setImageResource(it[2])
            }
        }


        binding.btnPlay.setOnClickListener {
            if (binding.radioGroup.checkedRadioButtonId != -1) {
                randomizeAndDisplayImages()
                decreaseFromAssets()
                addPointsWhenWin()
            } else {
                Toast.makeText(requireContext(), requireActivity().getString(R.string.no_bits), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.btnReset.setOnClickListener {
            viewModel.setCurrentAssetsFlow(100)

            binding.txtAssets.text = "${requireActivity().getString(R.string.assets)} ${viewModel.getCurrentAssetsFlow().value}$"
            binding.btnPlay.isEnabled = true
            binding.radioGroup.isEnabled = true
            binding.radioGroup.clearCheck()
            binding.edtSecretCode.text?.clear()
            viewModel.deleteAllGames()
            binding.chkMode.isChecked = false
        }

        binding.edtSecretCode.setOnEditorActionListener { _, _, _ ->
            if (binding.edtSecretCode.text.toString() == "ggodin") {
                binding.btnPlay.isEnabled = true
                viewModel.setCurrentAssetsFlow(viewModel.getCurrentAssetsFlow().value + 100)
                binding.txtAssets.text = "Your Assets: ${viewModel.getCurrentAssetsFlow().value}$"
                Toast.makeText(requireContext(), "secret code is Enabled", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Wrong Code", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun randomizeAndDisplayImages() {
        // Randomly select 3 unique image indices
        selectedIndices.clear()
        val random = java.util.Random()
        for (i in 0 until 3) {
            val randomIndex = random.nextInt(imageIds.size)
            selectedIndices.add(randomIndex)

        }
        viewModel.setCurrentImagesFlow(
            listOf(
                imageIds[selectedIndices[0]],
                imageIds[selectedIndices[1]],
                imageIds[selectedIndices[2]]
            )
        )
        // Set the selected images to the ImageViews
        binding.img1.setImageResource(imageIds[selectedIndices[0]])
        binding.img2.setImageResource(imageIds[selectedIndices[1]])
        binding.img3.setImageResource(imageIds[selectedIndices[2]])
    }

    private fun decreaseFromAssets() {
        val checkedRadioButton =
            requireActivity().findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)
        currentBet = extractNumberFromString(checkedRadioButton.text.toString())
        val currentAssets = viewModel.getCurrentAssetsFlow().value

        if (currentAssets - currentBet >= 0) {
            viewModel.decreaseFromAssets(currentBet)
            binding.btnPlay.isEnabled = true
        } else {
            Toast.makeText(requireContext(), requireActivity().getString(R.string.no_assets), Toast.LENGTH_SHORT)
                .show()
            binding.btnPlay.isEnabled = false

        }


    }

    private fun extractNumberFromString(inputString: String): Int {
        val numberPattern = Regex("\\d+")
        val matchResult = numberPattern.find(inputString)
        return matchResult?.value?.toInt() ?: 0
    }

    private fun addPointsWhenWin() {
        // normal mode
        if (!binding.chkMode.isChecked) {
            if (selectedIndices[0] == selectedIndices[1] || selectedIndices[0] == selectedIndices[2] || selectedIndices[1] == selectedIndices[2]) {
                if (selectedIndices[0] == selectedIndices[1] && selectedIndices[0] == selectedIndices[2]) {
                    val currentAssets = viewModel.getCurrentAssetsFlow().value
                    viewModel.setCurrentAssetsFlow(currentAssets + currentBet * 25)
//                    currentAssets += currentBet * 25
                    Toast.makeText(
                        requireContext(),
                        "${requireActivity().getString(R.string.win_amount)} ${currentBet * 25}",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveGame(
                        GameEntity(
                            img1 = imageIds[selectedIndices[0]],
                            img2 = imageIds[selectedIndices[1]],
                            img3 = imageIds[selectedIndices[2]],
                            currentAssets = currentAssets + currentBet * 25,
                            currentBet = currentBet,
                            isWin = true,
                            gain = currentBet * 25
                        )
                    )

                } else {
                    val currentAssets = viewModel.getCurrentAssetsFlow().value
                    viewModel.setCurrentAssetsFlow(currentAssets + currentBet * 1)

//                    currentAssets += currentBet * 1
                    Toast.makeText(
                        requireContext(),
                        "${requireActivity().getString(R.string.win_amount)} ${currentBet * 1}",
                        Toast.LENGTH_SHORT
                    ).show()

                    saveGame(
                        GameEntity(
                            img1 = imageIds[selectedIndices[0]],
                            img2 = imageIds[selectedIndices[1]],
                            img3 = imageIds[selectedIndices[2]],
                            currentAssets = currentAssets + currentBet * 1,
                            currentBet = currentBet,
                            isWin = true,
                            gain = currentBet * 1
                        )
                    )
                }
                binding.txtAssets.text = "${requireActivity().getString(R.string.assets)} ${viewModel.getCurrentAssetsFlow().value}$"
                Log.e("addPointsWhenWin: ", viewModel.getCurrentAssetsFlow().value.toString())
                //lose normal mode
            } else {

                saveGame(
                    GameEntity(
                        img1 = imageIds[selectedIndices[0]],
                        img2 = imageIds[selectedIndices[1]],
                        img3 = imageIds[selectedIndices[2]],
                        currentAssets = viewModel.getCurrentAssetsFlow().value,
                        currentBet = currentBet,
                        isWin = false,
                        lose = currentBet
                    )
                )
            }
        } else {

            // hard mode
            if ((selectedIndices[0] == 9 && selectedIndices[1] == 9) || (selectedIndices[0] == 9 && selectedIndices[2] == 9) || (selectedIndices[1] == 9 && selectedIndices[2] == 9)) {

                if (selectedIndices[0] == 9 && selectedIndices[1] == 9 && selectedIndices[2] == 9) {
                    val currentAssets = viewModel.getCurrentAssetsFlow().value
                    viewModel.setCurrentAssetsFlow(currentAssets + currentBet * 100)

                    Toast.makeText(
                        requireContext(),
                        "${requireActivity().getString(R.string.win_amount)} ${currentBet * 100}",
                        Toast.LENGTH_SHORT
                    ).show()

                    saveGame(
                        GameEntity(
                            img1 = imageIds[selectedIndices[0]],
                            img2 = imageIds[selectedIndices[1]],
                            img3 = imageIds[selectedIndices[2]],
                            currentAssets = currentAssets + currentBet * 100,
                            currentBet = currentBet,
                            isWin = true,
                            gain = currentBet * 100
                        )
                    )
                } else {
                    val currentAssets = viewModel.getCurrentAssetsFlow().value
                    viewModel.setCurrentAssetsFlow(currentAssets + currentBet * 10)

                    Toast.makeText(
                        requireContext(),
                        "${requireActivity().getString(R.string.win_amount)} ${currentBet * 10}",
                        Toast.LENGTH_SHORT
                    ).show()

                    saveGame(
                        GameEntity(
                            img1 = imageIds[selectedIndices[0]],
                            img2 = imageIds[selectedIndices[1]],
                            img3 = imageIds[selectedIndices[2]],
                            currentAssets = currentAssets + currentBet * 10,
                            currentBet = currentBet,
                            isWin = true,
                            gain = currentBet * 10
                        )
                    )
                }
                binding.txtAssets.text = "${requireActivity().getString(R.string.assets)} ${viewModel.getCurrentAssetsFlow().value}$"
                //lose hard mode
            } else {

                saveGame(
                    GameEntity(
                        img1 = imageIds[selectedIndices[0]],
                        img2 = imageIds[selectedIndices[1]],
                        img3 = imageIds[selectedIndices[2]],
                        currentAssets = viewModel.getCurrentAssetsFlow().value,
                        currentBet = currentBet,
                        isWin = false,
                        lose = currentBet
                    )
                )
            }
        }

    }


    private fun addMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.actionStatus -> {
                        findNavController().navigate(R.id.action_homeFragment_to_statusFragment)
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveGame(game: GameEntity) {
        lifecycleScope.launch {
            viewModel.insertGame(game)
        }

    }


}