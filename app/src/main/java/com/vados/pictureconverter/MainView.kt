package com.vados.pictureconverter

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Интерфейс для связи презентёра с View
 * AddToEndSingleStrategy - добавит пришедшую команду в конец очереди команд.
 * Если команда такого типа уже есть в очереди, то действующая удалится
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView